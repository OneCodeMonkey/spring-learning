package com.liuyang1.impl.utils;

import com.liuyang1.impl.utils.trace.LogMessage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 获取全局日志链路追踪参数，traceId，spanId，cspanId 等。
 */
public class TraceUtils {
    private final static ThreadLocal<Map<String, String>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static TraceInfo getTraceInfo() {
        String traceId = MDC.get("traceId");
        if (StringUtils.isBlank(traceId)) {
            traceId = LogMessage.generatorNewTraceid();
        }
        String spanId = MDC.get("spanId");
        if (StringUtils.isBlank(spanId)) {
            spanId = LogMessage.generatorNewSpanid();
        }

        String cspanId = MDC.get("cspanId");
        if (StringUtils.isBlank(cspanId)) {
            cspanId = LogMessage.generatorNewSpanid();
        }

        return new TraceInfo(traceId, spanId, cspanId);
    }

    public static void afterCompletion() {
        MDC.remove("cspanId");
    }

    public static <T> Callable<T> wapper(Callable<T> callable) {
        TraceInfo traceInfo = getTraceInfo();
        return () -> {
            preHandle(traceInfo);
            T result = callable.call();
            afterCompletion();
            return result;
        };
    }

    public static void preHandle(TraceInfo traceInfo) {
        MDC.put("traceId", traceInfo.traceId);
        MDC.put("spanId", traceInfo.spanId);
        MDC.put("cspanId", traceInfo.cspanId);
    }

    public static Runnable wapper(Runnable task) {
        TraceInfo traceInfo = getTraceInfo();
        return () -> {
            preHandle(traceInfo);
            task.run();
            afterCompletion();
        };
    }

    /**
     * get current trace id if exists, otherwise generate a new trace id and return it
     *
     * @return
     */
    public static String traceId() {
        String traceId = MDC.get("traceId");
        if (StringUtils.isEmpty(traceId)) {
            traceId = LogMessage.generatorNewTraceid();
            MDC.put("traceId", traceId);
//            LogMessage.setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * set trace id if traceId is not empty, otherwise do nothing
     *
     * @param traceId
     */
    public static String traceId(String traceId) {
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put("traceId", traceId);
            return traceId;
        }
        return traceId();
    }

    /**
     * get current span id if exists, otherwise generate a new span id and return it
     *
     * @return
     */
    public static String spanId() {
        String spanId = MDC.get("spanId");
        if (StringUtils.isEmpty(spanId)) {
            spanId = LogMessage.generatorNewSpanid();
            MDC.put("spanId", spanId);
        }
        return spanId;
    }

    /**
     * set span id if spanId is not empty, otherwise do nothing
     *
     * @param spanId
     */
    public static String spanId(String spanId) {
        if (StringUtils.isNotEmpty(spanId)) {
            MDC.put("spanId", spanId);
            return spanId;
        }
        return spanId();
    }

    /**
     * get current cspan id if exists, otherwise generate a new cspan id and return it
     *
     * @return
     */
    public static String cspanId() {
        String cspanId = MDC.get("cspanId");
        if (StringUtils.isEmpty(cspanId)) {
            cspanId = LogMessage.generatorNewSpanid();
            MDC.put("cspanId", cspanId);
        }
        return cspanId;
    }

    /**
     * set cspan id if cspanId is not empty, otherwise do nothing
     *
     * @param cspanId
     */
    public static void cspanId(String cspanId) {
        if (StringUtils.isNotEmpty(cspanId)) {
            MDC.put("cspanId", cspanId);
        }
    }

    public static void clear() {
        MDC.clear();
        THREAD_LOCAL.get().clear();
    }

    public static void add(String key, String value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static Map<String, String> get() {
        return THREAD_LOCAL.get();
    }

    @AllArgsConstructor
    public static class TraceInfo {
        private String traceId;
        private String spanId;
        private String cspanId;
    }
}
