package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stop {

    private Long stopId;

    private String trainCode;

    private Integer index;

    private String name;

    private Time arrivalTime;

    private Time departureTime;

}
