package com.liuyang1.impl.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JaxLiu
 * @date 2021-12-28
 * @description
 */
public enum HUBErrorCode {
    /**
     * ==== 公共的错误码用XXX 表示 ====
     **/
    SUCCESS(0, "", "success"),
    SUCCESS_OK(200, "", "success"),
    INVALID_PARAM(100, "Invalid parameter", "参数错误,请稍后重试"),
    CITY_NOT_EXIST(400, "cityid not exist", "cityid不存在"),
    SSO_USER_NOT_EXIST(401, "sso-user not exist", "sso-user 不存在"),
    RESUBMIT(402, "resubmit", "请勿重复提交"),
    SQL_LIMIT_TIME(403, "resubmit", "sql执行超时"),
    BIZ_VALIDATE_ERROR(405, "resubmit", "业务验证错误"),
    SQL_QUERY_FAIL(406, "query failed", "查询失败"),
    INTERNAL_ERROR(500, "internal Error!", "服务器内部错误，请稍后再试！"),

    /** 错误码格式详见： http://wiki.intra.xiaojukeji.com/pages/viewpage.action?pageId=393276317*/
    /**
     * 第1位（纬度）
     * 1:系统错误
     * 2.业务错误
     * 3.下游超时
     * 4.下游错误
     * 5. 参数错误
     * 6. 配置错误
     * 8. 业务告警
     * 9. 幂等成功
     * ---------------
     * 第2，3，4位（模块）
     * 005:订单
     * ---------------
     * 第5，6位（场景）
     * 00-不区分场景,01-导购,02-购物车,03-下单,04-支付完成,05-超时关单,06-团长,07-售后
     * ---------------
     * 第7，8，9位（自增错误码）
     */

    /**
     * 1:系统错误
     */
    HUB_UNKNOWN_ERROR(100500999, "unknown error", "公共未知错误"),
    HUB_CREATE_ORDER_UNKNOWN_ERROR(100503999, "unknown error", "服务器内部错误，请稍后再试！"),
    HUB_EXECUTE_DB_ERROR(100500001, "execute db exception", "执行db异常"),
    HUB_SEND_MESSAGE_ERROR(100500002, "send mq error", "发送消息异常"),
    HUB_CACHE_ERROR(100500003, "cache error", "缓存异常"),
    QUERY_FAILED(100500005, "query failed", "查询失败"),
    TOO_MANY_REQUEST_EXCEPTIOIN(100500006, "too many request exceptioin", "当前访问人数过多，请稍后再试"),
    HUB_LOCK_ERROR(100500007, "lock error", "获取锁异常"),
    UPLOAD_EXCEL_TO_GIFT_ERROR(100500008, "upload excel to gift error", "上传excel至gift失败"),
    HUB_STREAM_TIMEOUT_ERROR(100500009, "hub stream handle timeout error", "hub流式识别超时"),

    /**
     * 2.业务错误
     */
    PARAMS_EMPTY(200500000, "params should not be empty", "参数不合法"),
    PARAMS_REPEAT(200500001, "params repeat", "请勿重复请求"),
    DOMAIN_VALID(200500002, "domain valid", "没有理解该意图"),
    REQUEST_TOO_OFTEN(200500003, "request too often", "请求太快啦"),
    DATE_ERROR(200500004, "date error", "日期非法"),
    REQUEST_OVER_QUOTA(200500006, "request over quota", "接口使用量超出"),


    /**
     * 3.下游超时
     */
    HUB_GET_APOLLO_EXCEPTION(300000001, "apollo get exception", "Apollo调用异常"),
    HUB_BPM_CALL_FAILURE(300503010, "bpm call faiilure", "预算审批失败"),
    USER_GET_PERMISSIONS_API_CALL_FAIL(300503011, "USER_GET_PERMISSIONS_API_CALL_FAIL", "用户权限查询失败"),
    USER_GET_PERMISSIONS_API_ERR_NO_FAIL(300503012, "USER_GET_PERMISSIONS_API_ERR_NO_FAIL", "用户权限返回值有误"),


    /**
     * 5. 参数错误
     */
    HUB_PARAMS_ERROR(500500001, "参数有误.", "参数不能为空！"),
    HUB_SOURCE_TYPE_INVALID(500500002, "参数有误.", "sourceType不能为空！"),
    HUB_TOKEN_INVALID(500500003, "token有误.", "token不能为空！"),
    HUB_DOMAIN_EMPTY(500500004, "domainList不能为空.", "domainList不能为空！"),
    HUB_INTENT_EMPTY(500500005, "intentList不能为空.", "intentList不能为空！"),
    HUB_INTENT_VALID(500500006, "intent不支持.", "该场景意图不支持！"),
    HUB_TRACEID_INVALID(500500007, "traceId不能为空.", "traceId不能为空！"),
    REDIS_KEY_FORMAT_INVALID(500500008, "redis key格式错误", "redis key格式错误"),

    /**
     * 6. 配置错误
     */
    APOLLO_CONFIG_ERROR(600500001, "apollo config error", "apollo配置错误"),
    APOLLO_DEFAULT_DOMAIN_CONFIG_ERROR(600500002, "apollo default domain value  config error", "请检查apollo defaultDomain配置"),
    APOLLO_EXCLUDE_DOMAIN_CONFIG_ERROR(600500003, "apollo exclude domain config error", "请在apollo配置排除过滤的垂域"),


    /**
     * 8 业务告警
     */
    HUB_NLU_SERVICE_FLAG(700500001, "default query return", "采用HUB兜底回复"),

    /**
     * 9. 幂等成功
     */
    HUB_PAY_DEALING(900503001, "paying please retry later", "您的操作频率过快,请稍后再试"),

    /**
     * 业务归一化到端上可识别的统一错误码
     */
    SERVICE_EXCEPTION(3001, "service exception", ""),
    OTHER_EXCEPTION(5000, "other exception", "抱歉，我遇到些问题，请稍后再试试吧");

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

    private static Map<Integer, HUBErrorCode> map = null;

    public synchronized static void init() {
        if (map != null) {
            return;
        }
        map = new HashMap<>();
        for (HUBErrorCode eCode : HUBErrorCode.values()) {
            map.put(eCode.getCode(), eCode);
        }
    }

    private HUBErrorCode(int code, String logMsg, String message) {
        this.code = code;
        this.logMsg = logMsg;
        this.message = message;
    }

    public static String parse(int code) {
        HUBErrorCode eCode = getErrorEnum(code);
        return eCode.name();
    }

    public static HUBErrorCode getErrorEnum(int code) {
        if (map == null) {
            init();
        }
        HUBErrorCode eCode = map.get(code);
        return eCode;
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
