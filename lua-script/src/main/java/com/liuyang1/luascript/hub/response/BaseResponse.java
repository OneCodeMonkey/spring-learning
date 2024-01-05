package com.liuyang1.luascript.hub.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private String traceId;
    private T data;
    private T result;
    private int errno;
    private String errmsg;
}
