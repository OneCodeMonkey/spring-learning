package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

// todo
@Slf4j
public class ZSetOperationTest extends RedisBasicTest {
    @Test
    public void testZSetOperation() {
        Jedis jedis = jedisPool.getResource();

        // 1.zadd, zcard

        // 2.zscore

        // 3.zincrby

        // 4. zrange

        // 5. zrangeByScore

        // 6. zrangeByScoreWithScores

        // 7. zrank

        // 8. zrevrank

        // 9. zrem

        // 10. zrange

        // 11. zrangeWithScores

        // 12. zcount

        // 13. 集合类操作：
        // 13.1 zdiff, zdiffstore
        // 13.2 zinter, zinterstore
        // 13.3 zunion, zunionstore
        //
    }

    @AfterAll
    public static void tearDown() {
        Jedis jedis = jedisPool.getResource();
        jedis.del("key1");
        jedis.del("key2");
        jedis.del("key3");
        jedis.del("key4");
        jedis.del("key5");
        jedis.del("key6");
    }
}
