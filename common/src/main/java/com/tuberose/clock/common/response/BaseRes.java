package com.tuberose.clock.common.response;

import com.tuberose.clock.common.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BaseRes<T> {

    private final int code;
    private final String msg;
    private T data;

    private BaseRes() {
        code = ErrorCodeEnum.SUCCESS.getCode();
        msg = ErrorCodeEnum.SUCCESS.getMsg();
    }

    private BaseRes(ErrorCodeEnum state) {
        code = state.getCode();
        msg = state.getMsg();
    }

    private BaseRes(T data) {
        this();
        this.data = data;
    }

    private BaseRes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseRes<Void> success() {
        return new BaseRes<>();
    }

    public static <T> BaseRes<T> success(T data) {
        return new BaseRes<>(data);
    }

    public static BaseRes<Void> error(ErrorCodeEnum state) {
        return new BaseRes<>(state);
    }

    public static BaseRes<Void> error() {
        return new BaseRes<>(ErrorCodeEnum.SYSTEM_ERROR);
    }

    public static BaseRes<Void> error(int code, String msg) {
        return new BaseRes<>(code, msg);
    }

}
