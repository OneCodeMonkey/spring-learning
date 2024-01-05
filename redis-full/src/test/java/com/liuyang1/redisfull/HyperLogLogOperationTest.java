package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class HyperLogLogOperationTest extends RedisBasicTest {
    /**
     * HyperLogLog 是基于概率统计原理的 一个高效的计算算法，用于快速统计集合内的元素个数，快速进行去重统计。统计可以做集合合并计数的操作
     *
     * @see：https://blog.csdn.net/ldw201510803006/article/details/126093455
     */

    @Test
    public void testHyperLogLogOperation() {
        Jedis jedis = jedisPool.getResource();

        // 1.pfadd & pfcount
        long pfaddResult = jedis.pfadd("bigSet1", "name1", "name2", "name3");
        // pfadd 执行成功返回 1
        Assertions.assertEquals(1, pfaddResult);
        long count1 = jedis.pfcount("bigSet1");
        Assertions.assertEquals(3, count1);
        jedis.pfadd("bigSet1", "name2");
        long count2 = jedis.pfcount("bigSet1");
        Assertions.assertNotEquals(4, count2);
        jedis.pfadd("bigSet1", "name4");
        long count3 = jedis.pfcount("bigSet1");
        Assertions.assertEquals(4, count3);

        // 2.pfmerge
        jedis.del("bigSet1");
        jedis.pfadd("bigSet2", "name11", "name22", "name33");
        jedis.pfadd("bigSet3", "name111", "name222", "name333", "name22");
        Assertions.assertEquals(3, jedis.pfcount("bigSet2"));
        Assertions.assertEquals(4, jedis.pfcount("bigSet3"));
        String mergeResult = jedis.pfmerge("bigSet1", "bigSet2", "bigSet3");
        Assertions.assertEquals("OK", mergeResult);
        long count4 = jedis.pfcount("bigSet1");
        Assertions.assertEquals(6, count4);

        // 3.批量操作 pfadd 和 pfcount
        Pipeline pipeline = jedis.pipelined();
        jedis.del("bigSet1");
        Response<Long> result1 = pipeline.pfadd("bigSet1", "name1", "name2", "name3");
        Response<Long> result2 = pipeline.pfcount("bigSet1");
        pipeline.sync();
        Assertions.assertEquals(1, result1.get());
        Assertions.assertEquals(3, result2.get());
    }

    @AfterAll
    public static void tearDown() {
        Jedis jedis = jedisPool.getResource();
        jedis.del("bigSet1");
        jedis.del("bigSet2");
        jedis.del("bigSet3");
    }
}
