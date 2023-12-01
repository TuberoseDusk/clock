package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCarriage {
    private Long dailyCarriageId;

    private LocalDate date;
    private String trainCode;
    private Integer index;

    private Integer type;
}
