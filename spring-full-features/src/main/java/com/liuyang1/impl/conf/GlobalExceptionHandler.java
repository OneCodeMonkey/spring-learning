package com.liuyang1.impl.conf;

import com.liuyang1.impl.response.BaseResponse;
import com.liuyang1.impl.response.ErrorCode;
import com.liuyang1.impl.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
@Order(0)
public class GlobalExceptionHandler {

    String traceId = Md5Utils.getMD5Str(String.valueOf(System.nanoTime()));

    /**
     * 默认 Exception 异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse globalException(Exception e) {
        log.error("occur global Exception: {}, stackTrace: {}", e.getMessage(), e.getStackTrace());
        return BaseResponse.ofFail(ErrorCode.UNKNOWN_ERROR.getCode(),
                e.getMessage(), traceId);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public BaseResponse<Object> DuplicateKeyException(Exception e) {
        log.error("MySQL exception", e);
        return BaseResponse.ofFail(ErrorCode.INTERNAL_ERROR.getCode(), "已存在相同记录，请勿重复添加", traceId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Controller param exception", e);
        BindingResult result = e.getBindingResult();
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                list.add(p.getDefaultMessage());
            });
        }
        return BaseResponse.ofFail(ErrorCode.INTERNAL_ERROR.getCode(),
                list.size() == 1 ? list.get(0) : list.toString(), traceId, new ArrayList<>());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<Object> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("Controller param exception", e);
        return BaseResponse.ofFail(ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage(), traceId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse<Object> illegalArgumentException(IllegalArgumentException e) {
        log.error("biz argument exception", e);
        return BaseResponse.ofFail(ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage(), traceId);
    }

}
