package com.liuyang1.impl.utils.trace;


import org.slf4j.MDC;

public class ExtElement {
    public static final long PRODUCTION_TRAFFIC = 0L;
    public static final long COMMON_PRESSURE_TRAFFIC = 1L;
    private String callerFunc = "";
    private Long hintCode;
    private String hintContent;

    public ExtElement() {
    }

    public boolean isPressureTraffic() {
        if (this.hintCode == null) {
            return false;
        } else {
            return (this.hintCode & 1L) != 0L;
        }
    }

    public String getTraceId() {
        return MDC.get("traceId");
    }

    public String getSpanId() {
        return MDC.get("spanId");
    }

    public String getCspanId() {
        return MDC.get("cspanId");
    }

    public String getCallerFunc() {
        return this.callerFunc;
    }

    public void setCallerFunc(String callerFunc) {
        this.callerFunc = callerFunc;
    }

    public String getHintContent() {
        return this.hintContent;
    }

    public void setHintContent(String hintContent) {
        this.hintContent = hintContent;
    }

    public Long getHintCode() {
        return this.hintCode;
    }

    public void setHintCode(Long hintCode) {
        this.hintCode = hintCode;
    }
}
