package com.liuyang1.impl.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AutoLogImpl {
    private ThreadLocal<Long> timestamps = new ThreadLocal<>();
    private ThreadLocal<String> classNameMap = new ThreadLocal<>(), methodNameMap = new ThreadLocal<>();

    @Pointcut("@annotation(com.liuyang1.impl.annotation.AutoLog)")
    private void cut() {
    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        timestamps.set(startTime);

        // 反射，获取类名方法名参数列表，返回值类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String className = method.getDeclaringClass().getSimpleName(), methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        String argStr = String.join(", ",
                java.util.Arrays.stream(args).map(arg -> arg != null ? arg.toString() : "null").toArray(String[]::new)
        );

        Class<?> returnType = method.getReturnType();
        String returnClassName = returnType.getSimpleName();
        classNameMap.set(className);
        methodNameMap.set(methodName);

        log.info("method starting: {}@{}, returnType: {}, args: {}", className, methodName, returnClassName,
                argStr);
    }

    @After("cut()")
    public void after() {
        long startTime = timestamps.get(), cost = System.currentTimeMillis() - startTime;
        String className = classNameMap.get(), methodName = methodNameMap.get();
        log.info("method {}@{} finished. cost: {}ms", className, methodName, cost);
    }
}
