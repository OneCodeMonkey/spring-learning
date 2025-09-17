package com.liuyang1.spring_learning.dynamic_rate_limit.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author：didi
 * Date: 6/11/24 16:36
 * Desc: todo-add 描述
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private TraceLogInterceptor traceLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceLogInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
