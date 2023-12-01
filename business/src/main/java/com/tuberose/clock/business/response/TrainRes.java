package com.tuberose.clock.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRes {
    private Long trainId;
    private String code;
    private Integer type;

    private String startStation;
    private String startStationAbbrev;
    private Integer startTime;

    private String endStation;
    private String endStationAbbrev;
    private Integer endTime;
}
