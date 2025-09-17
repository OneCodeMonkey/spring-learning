package com.liuyang1.impl.aspect;

import com.liuyang1.impl.annotation.RateLimit;
import com.liuyang1.impl.enums.HUBErrorCode;
import com.liuyang1.impl.exception.BizException;
import com.liuyang1.impl.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@Slf4j
public class RedisRateLimiterAspect {
    @Resource
    private RedisKeyUtils redisKeyUtils;

    private final RedisRateLimiter rateLimiter;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public RedisRateLimiterAspect(RedisRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // 解析SpEL表达式（支持方法参数动态生成key）
        // todo-解析表达式入参，动态生成 key
        String key = handleKey(rateLimit.key());
        log.info("RedisRateLimiterAspect key: {}", key);

        // 计算时间窗口毫秒数
        long expireTime = rateLimit.timeUnit().toSeconds(rateLimit.period());

        if (!rateLimiter.tryAcquire(key, rateLimit.limit(), expireTime)) {
            log.error("RedisRateLimiterAspect, reach limit end!!");
            // metrics
//            PrometheusMetrics.record("", "rate-limit", rateLimit.key(), "rate_exceeded", "401", 0.001);
            throw new BizException(HUBErrorCode.REQUEST_TOO_OFTEN);
        }

        return joinPoint.proceed();
    }

    private String handleKey(String key) {
        String commonPrefix = "";
        try {
            commonPrefix = redisKeyUtils.getPrefix(RedisKeyUtils.TYPE_CACHE, RedisKeyUtils.DATA_TYPE_STRING);
        } catch (Exception ignored) {
            //
        }
        return commonPrefix + "rate_limit#" + key.trim().replace(",", "").replace(":", "#");
    }
}
