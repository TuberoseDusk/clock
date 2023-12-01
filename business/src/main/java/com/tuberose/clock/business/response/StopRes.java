package com.tuberose.clock.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopRes {
    private Long stopId;

    private String trainCode;

    private Integer index;

    private String name;

    private Integer arrivalTime;

    private Integer departureTime;
}
