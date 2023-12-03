package com.tuberose.clock.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStateEnum {

    ORDER_SUBMITTED(0, "已提交"),
    ORDER_CONFIRMED(1, "待支付"),
    ORDER_PAID(2, "已支付"),
    ORDER_NOT_PAID(3, "支付失败");

    private final Integer code;
    private final String desc;
}
