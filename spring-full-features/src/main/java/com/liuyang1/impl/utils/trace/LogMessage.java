package com.liuyang1.impl.utils.trace;

import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogMessage {
    public static String HEADER_TRACEID = "didi-header-rid";
    public static String HEADER_SPANID = "didi-header-spanid";
    private static String DEFAULT_DLTAG = "_undef";
    private static ThreadLocal<ExtElement> extElements = new InheritableThreadLocal<ExtElement>() {
        protected ExtElement initialValue() {
            return new ExtElement();
        }
    };
    private String dltag = "";
    private String cspanId = "";
    private List<LogKV> logElements = new ArrayList();

    public LogMessage() {
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }

    public static String getSpanId() {
        return MDC.get("spanId");
    }

    public static String getNewCspanId() {
        return ((ExtElement) extElements.get()).getCspanId();
    }

    public static String getCallerFunc() {
        return ((ExtElement) extElements.get()).getCallerFunc();
    }

    public static void setCallerFunc(String callerFunc) {
        ((ExtElement) extElements.get()).setCallerFunc(callerFunc);
    }

    public static Long getHintCode() {
        return ((ExtElement) extElements.get()).getHintCode();
    }

    public static void setHintCode(Long hintCode) {
        ((ExtElement) extElements.get()).setHintCode(hintCode);
    }

    public static String getHintContent() {
        return ((ExtElement) extElements.get()).getHintContent();
    }

    public static void setHintContent(String hintContent) {
        ((ExtElement) extElements.get()).setHintContent(hintContent);
    }

    public static boolean isPressureTraffic() {
        return ((ExtElement) extElements.get()).isPressureTraffic();
    }

    public static String generatorNewSpanid() {
        String newSpanid = SpanIdGenerator.getSpanId();
        return newSpanid;
    }

    public static String generatorNewTraceid() {
        String traceid = SpanIdGenerator.getTraceId();
        return traceid;
    }

    public static void remove() {
        extElements.remove();
    }

    public static void setExtElements(ThreadLocal<ExtElement> extElements) {
        LogMessage.extElements = extElements;
    }

    public static String genertorNewTraceid() {
        String traceid = SpanIdGenerator.getTraceId();
        return traceid;
    }

    public LogMessage add(String key, Object value) {
        LogKV logKV = new LogKV(key, value);
        this.logElements.add(logKV);
        return this;
    }

    public List<LogKV> getLogElements() {
        return this.logElements;
    }

    public void setLogElements(List<LogKV> logElements) {
        this.logElements = logElements;
    }

    public String getDltag() {
        return this.dltag != null && !this.dltag.equals("") ? this.dltag : DEFAULT_DLTAG;
    }

    public LogMessage setDltag(String dltag) {
        this.dltag = dltag;
        return this;
    }

    public String getCspanId() {
        return this.cspanId;
    }

    public LogMessage setCspanId(String cspanId) {
        this.cspanId = cspanId;
        return this;
    }

    public String toString() {
        ExtElement extElement = (ExtElement) extElements.get();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getDltag());
        sb.append("||traceid=");
        sb.append(extElement.getTraceId());
        sb.append("||spanid=");
        sb.append(extElement.getSpanId());
        sb.append("||cspanid=");
        sb.append(this.getCspanId());
        if (this.logElements.size() > 0) {
            Iterator var3 = this.logElements.iterator();

            while (var3.hasNext()) {
                LogKV param = (LogKV) var3.next();
                sb.append("||");
                sb.append(param.getKey());
                sb.append("=");
                sb.append(param.getValue());
            }
        }

        return sb.toString();
    }
}

