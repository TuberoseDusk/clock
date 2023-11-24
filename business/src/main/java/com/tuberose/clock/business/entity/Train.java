package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    private Long trainId;
    private String code;
    private Integer type;

    private String startStation;
    private String startStationAbbrev;
    private Time startTime;

    private String endStation;
    private String endStationAbbrev;
    private Time endTime;
}
