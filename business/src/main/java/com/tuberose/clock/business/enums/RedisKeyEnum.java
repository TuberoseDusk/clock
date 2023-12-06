package com.tuberose.clock.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisKeyEnum {

    DAILY_SECTION_CACHE("DAILY_SECTION_CACHE/"),
    ORDER_CONFIRM("ORDER_CONFIRM/");

    private final String keyPrefix;
}
