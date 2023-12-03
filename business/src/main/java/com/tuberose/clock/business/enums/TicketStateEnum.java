package com.tuberose.clock.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicketStateEnum {

    NO_TICKET_ISSUED(0, "未出票"),
    TICKET_NOT_USED(1, "未使用"),
    TICKET_CHECKED(2, "已检票"),
    TICKET_USED(2, "已使用"),
    TICKET_REFUNDED(3, "已退款"),
    TICKET_RESCHEDULED(4, "已改签");

    private final Integer code;
    private final String desc;
}
