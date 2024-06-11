package com.liuyang1.spring_learning.dynamic_rate_limit.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * Author：didi
 * Date: 6/11/24 16:26
 * Desc:
 */
@Slf4j
@Component
public class JedisPoolResource {
    JedisPool jedisPool;

    private final static String redisIp = "127.0.0.1";
    private final static int redisPort = 6379;
    private final static String redisPassword = "your-password";
    private final static int redisTimeout = 3000;

    @PostConstruct
    public void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(200);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(3000));
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        String connectionString = String.format("redis://:%s@%s:%d", redisPassword, redisIp, redisPort);
        jedisPool = new JedisPool(jedisPoolConfig, connectionString);
//        jedisPool = new JedisPool(jedisPoolConfig, redisIp, redisPort, redisTimeout, "", redisPassword);
    }

    public JedisPool getPoolConnection() {
        return jedisPool;
    }
}
