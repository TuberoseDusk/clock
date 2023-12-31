package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stop {

    private Long stopId;

    private String trainCode;

    private Integer index;

    private String name;

    private Integer arrivalTime;

    private Integer departureTime;

}
