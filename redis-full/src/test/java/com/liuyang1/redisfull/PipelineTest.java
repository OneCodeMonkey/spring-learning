package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.List;

/**
 * Author：didi
 * Date: 5/11/24 16:21
 * Desc: todo-add 描述
 */
@Slf4j
public class PipelineTest extends RedisBasicTest {
    @Test
    public void testPipelineUsage() {
        Jedis jedis = jedisPool.getResource();

        Pipeline pipeline = jedis.pipelined();
        pipeline.set("key1", "1");
        pipeline.del("key1");
        pipeline.set("key2", "1");
        pipeline.del("key2");
        pipeline.set("key3", "1");
        pipeline.del("key3");
        // ["OK",1,"OK",1,"OK",1]
        List<Object> result = pipeline.syncAndReturnAll();

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals("OK", result.get(0));
        Assertions.assertEquals(1L, result.get(1));
        Assertions.assertEquals("OK", result.get(2));
    }

    @Test
    public void testPipelineSpeed() {
        Jedis jedis = jedisPool.getResource();
        final int keySize = 1000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < keySize; i++) {
            jedis.set("key" + i, "1");
        }
        for (int i = 0; i < keySize; i++) {
            jedis.del("key" + i);
        }
        long cost1 = System.currentTimeMillis() - startTime;
        log.info("size: {}, normal redis operation cost: {}", keySize, cost1);
        startTime = System.currentTimeMillis();

        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < keySize; i++) {
            pipeline.set("key" + i, "1");
        }
        for (int i = 0; i < keySize; i++) {
            pipeline.del("key" + i);
        }
        pipeline.sync();
        long cost2 = System.currentTimeMillis() - startTime;
        log.info("size: {}, pipeline redis operation cost: {}", keySize, cost2);

        Assertions.assertTrue(cost1 > 0);
        Assertions.assertTrue(cost2 > 0);
        Assertions.assertTrue(cost2 < cost1 / 2);

        // 日志 sample
//        2024-05-14 16:01:14.981  INFO 18518 --- [           main] com.liuyang1.redisfull.PipelineTest      : size: 1000, normal redis operation cost: 46394
//        2024-05-14 16:01:15.183  INFO 18518 --- [           main] com.liuyang1.redisfull.PipelineTest      : size: 1000, pipeline redis operation cost: 172
    }

    @Test
    public void testPipelineMiddleGetResult() {
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        pipeline.set("key1", "1");
        pipeline.set("key2", "1");
        pipeline.set("key3", "1");

        // 验证抛出异常
        Assertions.assertDoesNotThrow(() -> {
            pipeline.set("key3", "3");
        });
        Response<String> resp = pipeline.get("key1");
        // 抛出异常
        Assertions.assertThrows(IllegalStateException.class, resp::get);

        pipeline.del("key1");
        pipeline.del("key2");
        pipeline.del("key3");
        List<Object> result = pipeline.syncAndReturnAll();
//        log.info(new Gson().toJson(result));
        Assertions.assertEquals(8, result.size());
    }

    @AfterAll
    public static void tearDown() {
        Jedis jedis = jedisPool.getResource();
        jedis.del("key1");
        jedis.del("key2");
        jedis.del("key3");
        jedis.del("key4");
        jedis.del("key5");
    }
}
