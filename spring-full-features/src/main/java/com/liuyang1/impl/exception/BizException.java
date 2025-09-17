package com.liuyang1.impl.exception;

import com.liuyang1.impl.enums.HUBErrorCode;

/**
 * @author: hxh
 * @date: 2020/7/10
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 4360479572003243839L;

    private Integer errno;
    private String errmsg;

    public BizException(HUBErrorCode hubErrorCode) {
        super();
        this.errno = hubErrorCode.getCode();
        this.errmsg = hubErrorCode.getMessage();
    }

    public BizException(Integer errno, String errmsg) {
        super();
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public Integer getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }
}
