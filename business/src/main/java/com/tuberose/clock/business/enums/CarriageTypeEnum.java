package com.tuberose.clock.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CarriageTypeEnum {

    FIRST_CLASS_CARRIAGE(1, "一等座"),
    SECOND_CLASS_CARRIAGE(2, "二等座");

    private final Integer code;
    private final String desc;
}
