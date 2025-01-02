package com.liuyang1.impl.response;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    /**
     * ==== 公共的错误码用XXX 表示 ====
     **/
    SUCCESS(0, "", "success"),
    SUCCESS_OK(200, "", "success"),
    INVALID_PARAM(100, "Invalid parameter", "参数错误,请稍后重试"),
    SSO_USER_NOT_EXIST(401, "sso-user not exist", "sso-user 不存在"),
    RESUBMIT(402, "resubmit", "请勿重复提交"),
    SQL_LIMIT_TIME(403, "resubmit", "sql执行超时"),
    BIZ_VALIDATE_ERROR(405, "resubmit", "业务验证错误"),
    SQL_QUERY_FAIL(406, "query failed", "查询失败"),
    CUSTOM_HTTP_AUTH_FAIL(407, "auth failed", "http认证失败"),
    INTERNAL_ERROR(500, "internal Error!", "服务器内部错误，请稍后再试！"),

    /**
     * 1:系统错误
     */
    UNKNOWN_ERROR(100500999, "unknown error", "公共未知错误"),
    QUERY_FAILED(100500005, "query failed", "查询失败"),
    TOO_MANY_REQUEST_EXCEPTIOIN(100500006, "too many request exceptioin", "当前访问人数过多，请稍后再试"),
    LOCK_ERROR(100500007, "lock error", "获取锁异常"),
    UPLOAD_EXCEL_TO_GIFT_ERROR(100500008, "upload excel to gift error", "上传excel至gift失败"),

    /**
     * 2.业务错误
     */
    PARAMS_EMPTY(200500000, "params should not be empty", "参数不合法"),
    PARAMS_REPEAT(200500001, "params repeat", "请勿重复请求"),
    //    DOMAIN_VALID(200500002, "domain valid", "没有理解该意图"),
    REQUEST_TOO_OFTEN(200500003, "request too often", "请求太快啦"),

    /**
     * 3.下游超时
     */
    GET_APOLLO_EXCEPTION(300000001, "apollo get exception", "Apollo调用异常"),
    BPM_CALL_FAILURE(300503010, "bpm call faiilure", "预算审批失败"),
    USER_GET_PERMISSIONS_API_CALL_FAIL(300503011, "USER_GET_PERMISSIONS_API_CALL_FAIL", "用户权限查询失败"),
    USER_GET_PERMISSIONS_API_ERR_NO_FAIL(300503012, "USER_GET_PERMISSIONS_API_ERR_NO_FAIL", "用户权限返回值有误"),

    /**
     * 4.下游错误
     */
//    HUB_CALL_OPEN_PLATFORM_ERROR(400000000, "hub call open platform redis config exception ", "调用redis获取垂域意图配置异常"),

    /**
     * 5. 参数错误
     */
    PARAMS_ERROR(500500001, "参数有误.", "参数不能为空！"),

    /**
     * 6. 配置错误
     */
    APOLLO_CONFIG_ERROR(600500001, "apollo config error", "apollo配置错误"),

    /**
     * 8 业务告警
     */

    /**
     * 9. 幂等成功
     */
    HUB_PAY_DEALING(900503001, "paying please retry later", "您的操作频率过快,请稍后再试");

    private static Map<Integer, ErrorCode> map = null;
    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误日志信息
     */
    private String logMsg;
    /**
     * 展示在前端的错误提醒信息
     */
    private String message;

    private ErrorCode(int code, String logMsg, String message) {
        this.code = code;
        this.logMsg = logMsg;
        this.message = message;
    }

    public static synchronized void init() {
        if (map != null) {
            return;
        }
        map = new HashMap<>();
        for (ErrorCode eCode : ErrorCode.values()) {
            map.put(eCode.getCode(), eCode);
        }
    }

    public static String parse(int code) {
        ErrorCode eCode = getErrorEnum(code);
        return eCode.name();
    }

    public static ErrorCode getErrorEnum(int code) {
        if (map == null) {
            init();
        }
        return map.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public String getMessage() {
        return message;
    }
}