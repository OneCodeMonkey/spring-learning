package com.liuyang1.impl.response;

import com.liuyang1.impl.utils.MD5Utils;
import lombok.Data;

@Data
public class BaseResponse<T> {
    private T result;
    private int errno;
    private String errmsg;
    private String traceId;

    public void setErrno(int errno) {
        if (errno == 200) {
            errno = 0;
        }
        this.errno = errno;
    }

    public BaseResponse() {
        this.errno = ErrorCode.SUCCESS.getCode();
        this.errmsg = ErrorCode.SUCCESS.getMessage();
        this.traceId = MD5Utils.getMD5Hash(String.valueOf(System.nanoTime()));
    }

    public static BaseResponse build() {
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    public static <T> BaseResponse<T> build(T result) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setResult(result);
        return baseResponse;
    }

    public static BaseResponse<Void> build(int code, String message) {
        BaseResponse baseResponse = new BaseResponse<>();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(null);
        return baseResponse;
    }

    public static <T> BaseResponse<T> ofFail(int code, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(null);
        return baseResponse;
    }

    public static <T> BaseResponse<T> ofFail(int code, String message, String traceId) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(null);
        baseResponse.setTraceId(traceId);
        return baseResponse;
    }

    public static <T> BaseResponse<T> ofFail(int code, String message, T result) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(result);
        return baseResponse;
    }

    public static <T> BaseResponse<T> ofFail(int code, String message, String traceId, T result) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(result);
        baseResponse.setTraceId(traceId);
        return baseResponse;
    }

    public static BaseResponse ofSuccess(int code, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setErrno(code);
        baseResponse.setErrmsg(message);
        baseResponse.setResult(null);
        return baseResponse;
    }

    public static BaseResponse ofFail() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setErrno(ErrorCode.UNKNOWN_ERROR.getCode());
        baseResponse.setErrmsg(ErrorCode.UNKNOWN_ERROR.getMessage());
        baseResponse.setResult(null);
        return baseResponse;
    }
}
