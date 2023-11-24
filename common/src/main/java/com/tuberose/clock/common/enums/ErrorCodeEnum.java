package com.tuberose.clock.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    SUCCESS(0, "成功"),
    SYSTEM_ERROR(-1, "未知错误"),

    USER_ERROR(1000, "用户模块错误"),
    USER_REGISTER_ERROR(1001, "用户注册错误"),
    USER_NAME_EXIST(1002, "用户名已存在"),
    USER_ACCOUNT_NOT_EXIST(1003, "用户账号不存在"),
    USER_PASSWORD_ERROR(1004, "用户密码错误"),

    PASSENGER_EXIST(1101, "乘客已存在"),
    PASSENGER_ID_MISSING(1102, "乘客ID丢失"),
    PASSENGER_NOT_EXIST(1103, "乘客不存在"),

    STATION_NAME_EXIST(2001, "车站名称已存在"),
    STATION_ABBREV_EXIST(2002, "车站缩写已存在"),
    STATION_NOT_EXIST(2003, "车站不存在"),

    TRAIN_CODE_EXIST(2101, "车次已存在"),

    REQUEST_PARAM_ERROR(9001, "输入参数错误");


    private final int code;
    private final String msg;
}
