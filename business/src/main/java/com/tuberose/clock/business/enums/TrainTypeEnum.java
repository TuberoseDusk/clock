package com.tuberose.clock.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TrainTypeEnum {

    HIGH_SPEED_RAIL(1, "G", "高铁"),
    BULLET_TRAIN(2, "D", "动车");

    private final Integer code;
    private final String tab;
    private final String desc;
}
