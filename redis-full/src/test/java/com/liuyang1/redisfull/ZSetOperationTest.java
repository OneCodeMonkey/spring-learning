package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import redis.clients.jedis.Jedis;

// todo
public class ZSetOperationTest extends RedisBasicTest {

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
