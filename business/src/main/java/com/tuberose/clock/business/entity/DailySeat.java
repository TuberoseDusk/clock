package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySeat {
    private Long dailySeatId;

    private Long dailyCarriageId;

    private String row;
    private String col;
    private Integer number;

    private String state;
}
