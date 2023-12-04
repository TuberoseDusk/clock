package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySection implements Serializable {
    private LocalDate date;
    private String trainCode;

    private String startStop;
    private LocalDateTime startTime;
    private Integer startStopIndex;

    private String endStop;
    private LocalDateTime endTime;
    private Integer endStopIndex;

    private Integer firstClassSeatCount;
    private BigDecimal firstClassSeatPrice;
    private Integer secondClassSeatCount;
    private BigDecimal secondClassSeatPrice;
}
