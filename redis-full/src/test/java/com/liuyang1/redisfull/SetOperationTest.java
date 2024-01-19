package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

public class SetOperationTest extends RedisBasicTest {
    @Test
    public void testSetOperation() {
        Jedis jedis = jedisPool.getResource();

        // 1.sadd, scard
        jedis.sadd("key1", "elem1", "elem2", "elem3");
        long count = jedis.scard("key1");
        Assertions.assertEquals(3, count);

        // 2.smembers
        jedis.del("key1");
        jedis.sadd("key1", "elem1", "elem2", "elem3");
        Set<String> set1 = jedis.smembers("key1");
        Assertions.assertEquals(3, set1.size());
        Assertions.assertTrue(set1.contains("elem1"));
        Assertions.assertTrue(set1.contains("elem2"));
        Assertions.assertTrue(set1.contains("elem3"));

        // 3.sismember
        jedis.del("key1");
        jedis.sadd("key1", "elem1", "elem2", "elem3");
        Assertions.assertTrue(jedis.sismember("key1", "elem1"));
        Assertions.assertTrue(jedis.sismember("key1", "elem2"));
        Assertions.assertTrue(jedis.sismember("key1", "elem3"));

        // 4.srandmember
        jedis.del("key2");
        jedis.sadd("key2", "elem11", "elem22", "elem33");
        for (int i = 0; i < 10; i++) {
            Set<String> set2 = new HashSet<>();
            set2.add("elem11");
            set2.add("elem22");
            set2.add("elem33");
            Assertions.assertTrue(set2.contains(jedis.srandmember("key2")));
        }

        // 5.srem
        jedis.del("key3");
        jedis.sadd("key3", "elem111", "elem222", "elem333");
        Assertions.assertEquals(3, jedis.scard("key3"));
        Assertions.assertTrue(jedis.sismember("key3", "elem222"));
        jedis.srem("key3", "elem222");
        Assertions.assertEquals(2, jedis.scard("key3"));
        Assertions.assertFalse(jedis.sismember("key3", "elem222"));

        // 6.sdiff: 两个集合，获取第一个集合中存在，且第二个中不存在的元素集合
        jedis.del("key4");
        jedis.del("key5");
        jedis.sadd("key4", "elem1", "elem2", "elem3");
        jedis.sadd("key5", "elem3", "elem4", "elem5");
        Assertions.assertEquals(3, jedis.scard("key4"));
        Assertions.assertEquals(3, jedis.scard("key5"));
        Set<String> diffSet = jedis.sdiff("key4", "key5");
        Assertions.assertEquals(2, diffSet.size());
        Assertions.assertTrue(diffSet.contains("elem1"));
        Assertions.assertTrue(diffSet.contains("elem2"));
        Assertions.assertFalse(diffSet.contains("elem3"));
        //
        // 6.1 sdiffstore: 获取两个集合的交集，并存储到第三个集合里
        jedis.sdiffstore("key6", "key4", "key5");
        Assertions.assertEquals(2, jedis.scard("key6"));
        Assertions.assertTrue(jedis.sismember("key6", "elem2"));
        Assertions.assertFalse(jedis.sismember("key6", "elem3"));

        // 6.2 如果第三个集合已存在？使用 sdiffstore 往里写入，会发生什么？
        jedis.del("key6");
        jedis.sadd("key6", "elem111", "elem222", "elem333");
        jedis.sdiffstore("key6", "key4", "key5");
//        Assertions.assertEquals(5, jedis.scard("key6"));  // 返回2
        Assertions.assertEquals(2, jedis.scard("key6"));
        Assertions.assertNotEquals(2 + 3, jedis.scard("key6"));
        // 结论：使用 sdiffstore，sinterstore，sunionstore，如果写入的目标 key 已经有集合存在，那么会被整体覆盖，原本的全部不存在。

        // 7.sinter：获取两个集合的交集
        jedis.del("key4");
        jedis.del("key5");
        jedis.sadd("key4", "elem1", "elem2", "elem3");
        jedis.sadd("key5", "elem3", "elem4", "elem5");
        Assertions.assertEquals(3, jedis.scard("key4"));
        Assertions.assertEquals(3, jedis.scard("key5"));
        Set<String> diffSet2 = jedis.sinter("key4", "key5");
        Assertions.assertEquals(1, diffSet2.size());
        Assertions.assertTrue(diffSet2.contains("elem3"));
        Assertions.assertFalse(diffSet2.contains("elem1"));
        Assertions.assertFalse(diffSet2.contains("elem5"));
        //
        // 7.1 sinterstore
        jedis.sinterstore("key6", "key4", "key5");
        Assertions.assertEquals(1, jedis.scard("key6"));
        Assertions.assertTrue(jedis.sismember("key6", "elem3"));
        Assertions.assertFalse(jedis.sismember("key6", "elem1"));
        Assertions.assertFalse(jedis.sismember("key6", "elem5"));

        // 8.sunion：获取两个集合的并集
        jedis.del("key4");
        jedis.del("key5");
        jedis.sadd("key4", "elem1", "elem2", "elem3");
        jedis.sadd("key5", "elem3", "elem4", "elem5");
        Assertions.assertEquals(3, jedis.scard("key4"));
        Assertions.assertEquals(3, jedis.scard("key5"));
        Set<String> diffSet3 = jedis.sunion("key4", "key5");
        Assertions.assertEquals(5, diffSet3.size());
        Assertions.assertTrue(diffSet3.contains("elem3"));
        Assertions.assertTrue(diffSet3.contains("elem1"));
        Assertions.assertTrue(diffSet3.contains("elem5"));
        //
        // 8.1 sunionstore
        jedis.sunionstore("key6", "key4", "key5");
        Assertions.assertEquals(5, jedis.scard("key6"));
        Assertions.assertTrue(jedis.sismember("key6", "elem3"));
        Assertions.assertTrue(jedis.sismember("key6", "elem1"));
        Assertions.assertTrue(jedis.sismember("key6", "elem5"));

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
