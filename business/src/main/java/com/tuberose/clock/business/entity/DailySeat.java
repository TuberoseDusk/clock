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

    private LocalDate date;
    private String trainCode;
    private Integer carriageIndex;
    private Integer type;

    private String row;
    private String col;
    private Integer number;
}
