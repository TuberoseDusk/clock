package com.tuberose.clock.common.exception;

import com.tuberose.clock.common.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCodeEnum state;

    public BusinessException(ErrorCodeEnum state) {
        super(state.getMsg(), null, false, false);
        this.state = state;
    }
}
