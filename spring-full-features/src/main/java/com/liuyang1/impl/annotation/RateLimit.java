package com.liuyang1.impl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式限流注解：用在 method 上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String key();

    int limit() default 10;

    int period() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
