package com.tuberose.clock.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopRes {
    private Long stopId;

    private String trainCode;

    private Integer index;

    private String name;

    private Time arrivalTime;

    private Time departureTime;
}
