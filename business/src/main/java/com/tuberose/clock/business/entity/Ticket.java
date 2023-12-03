package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long ticketId;

    private Long orderId;
    private Long passengerId;

    private String trainCode;

    private Integer carriageIndex;
    private Integer type;
    private String row;
    private String col;

    private String startStop;
    private LocalDateTime startTime;

    private String endStop;
    private LocalDateTime endTime;

    private BigDecimal price;

    private Integer state;
}
