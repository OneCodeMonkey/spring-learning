package com.liuyang1.impl.aspect;

import com.liuyang1.impl.annotation.DayCountLimit;
import com.liuyang1.impl.enums.HUBErrorCode;
import com.liuyang1.impl.exception.BizException;
import com.liuyang1.impl.redis.RedisProvider;
import com.liuyang1.impl.utils.MyDateUtils;
import com.liuyang1.impl.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@Slf4j
public class RedisDayCountLimiterAspect {
    @Resource
    private RedisProvider redisProvider;

    @Resource
    private RedisKeyUtils redisKeyUtils;

    @Around("@annotation(dayCountLimit)")
    public Object around(ProceedingJoinPoint joinPoint, DayCountLimit dayCountLimit) throws Throwable {
        //
        String key = handleKey(dayCountLimit.key());
        log.info("RedisDayCountLimiterAspect key: {}", key);

        // todo-add apollo 配置干预
        int limitCount = Math.max(0, dayCountLimit.limit());
        long count = 1;
        if (redisProvider.exist(key)) {
            count = Long.parseLong(redisProvider.get(key));
        } else {
            redisProvider.setpx(key, "1", 24 * 3600 * 1000L);
        }

        if (count > limitCount) {
            log.error("RedisDayCountLimiterAspect, reach day count limit!! {} -> {}", limitCount, count);
//            PrometheusMetrics.record("", "day-count-limit", dayCountLimit.key(), "day_count_exceeded", "402", 0.001);
            throw new BizException(HUBErrorCode.REQUEST_OVER_QUOTA);
        } else {
            long setRet = redisProvider.incr(key);
            if (setRet == 0) {
                log.error("RedisDayCountLimiterAspect, incr day_count_limit key failed!");
            }
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
        return commonPrefix + "day_limit#" + MyDateUtils.getCurrentFormattedDate("yyyyMMdd") + "#" +
                key.trim().replace(",", "").replace(":", "#");
    }
}
