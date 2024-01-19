package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Slf4j
public class RedisBasicTest extends RedisFullApplicationTests {
    private final static String redisIp = "127.0.0.1";
    private final static int redisPort = 6378;
    private final static String redisPassword = "password1";

    public static JedisPool jedisPool;

    @BeforeAll
    public static void setUp() {
        log.info("initializing pool...");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(50);
        poolConfig.setMaxWait(Duration.ofMillis(3000));
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);

        String connectionString = String.format("redis://:%s@%s:%d", redisPassword, redisIp, redisPort);
        jedisPool = new JedisPool(poolConfig, connectionString);
    }

    @Test
    public void testConnect() {
        try {
            Jedis jedis = jedisPool.getResource();
            Assertions.assertNotNull(jedis);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        jedisPool.close();
        log.info("shutdown pool");
    }
}
