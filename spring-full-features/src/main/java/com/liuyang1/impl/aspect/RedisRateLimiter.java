package com.liuyang1.impl.aspect;

import com.liuyang1.impl.redis.RedisProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RedisRateLimiter {
    @Resource
    private RedisProvider redisProvider;

    public RedisRateLimiter() {
    }

    public boolean tryAcquire(String key, int limit, long expireTimeSecs) {
        limit = Math.max(1, limit);
        expireTimeSecs = Math.max(1, expireTimeSecs);
        if (redisProvider.exist(key)) {
            long cur = redisProvider.incr(key);
            if (redisProvider.ttl(key) == -1) {
                redisProvider.setpx(key, "1", expireTimeSecs * 1000L);
            }
            return cur <= limit;
        } else {
            redisProvider.setpx(key, "1", expireTimeSecs * 1000L);
        }

        return true;
    }
}
