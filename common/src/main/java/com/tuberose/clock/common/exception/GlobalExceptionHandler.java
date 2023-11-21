package com.tuberose.clock.common.exception;

import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.response.BaseRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public BaseRes<Void> bindExceptionHandler(BindException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(msg);
        return BaseRes.error(ErrorCodeEnum.REQUEST_PARAM_ERROR.getCode(),
                ErrorCodeEnum.REQUEST_PARAM_ERROR.getMsg() + ": " + msg);
    }

    @ExceptionHandler(BusinessException.class)
    public BaseRes<Void> businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage());
        return BaseRes.error(e.getState());
    }

    @ExceptionHandler(Exception.class)
    public BaseRes<Void> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return BaseRes.error();
    }
}
