package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStop {
    private Long dailyStopId;

    private Long dailyTrainId;

    private Integer index;

    private String name;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;
}
