package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashOperationTest extends RedisBasicTest {
    @Test
    public void testHashOperations() {
        Jedis jedis = jedisPool.getResource();

        // 1.hset & hget
        jedis.hset("key1", "field1", "value1");
        String result1 = jedis.hget("key1", "field1");
        Assertions.assertEquals("value1", result1);

        // 2.hmset
        Map<String, String> params = new HashMap<>();
        params.put("field1", "value111");
        params.put("field2", "value222");
        jedis.hmset("key1", params);
        // hset 也支持批量操作 set，效果一样
        // jedis.hset("key1", params);
        Assertions.assertEquals("value111", jedis.hget("key1", "field1"));
        Assertions.assertEquals("value222", jedis.hget("key1", "field2"));

        // 3.hmget
        Map<String, String> params2 = new HashMap<>();
        params2.put("field1", "value111");
        params2.put("field2", "value222");
        jedis.hmset("key2", params2);
        List<String> values = jedis.hmget("key2", "field1", "field2");
        Assertions.assertNotNull(values);
        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals("value222", values.get(1));

        // 4.hgetAll()
        Map<String, String> params3 = new HashMap<>();
        params3.put("field1", "value111");
        params3.put("field2", "value222");
        jedis.hmset("key3", params3);
        Map<String, String> result2 = jedis.hgetAll("key3");
        Assertions.assertNotNull(result2);
        Assertions.assertEquals(2, result2.size());
        Assertions.assertEquals("value111", jedis.hget("key3", "field1"));

        // 5. hdel()
        Map<String, String> params4 = new HashMap<>();
        params4.put("field1", "value111");
        params4.put("field2", "value222");
        jedis.hmset("key4", params4);
        Map<String, String> result3 = jedis.hgetAll("key4");
        Assertions.assertNotNull(result3);
        Assertions.assertEquals(2, result3.size());
        jedis.hdel("key4", "field2");
        result3 = jedis.hgetAll("key4");
        Assertions.assertNotNull(result3);
        Assertions.assertEquals(1, result3.size());
        Assertions.assertEquals("value111", result3.get("field1"));
        Assertions.assertNull(result3.get("field2"));

        // todo 后续有‍精力补充
        // hexist()
        // hincrBy()
        // hincrByFloat()
        // hlen()
        // hkeys()
        // hvals()
        // hsetnx()
        // hstrlen()
        // hscan()
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
