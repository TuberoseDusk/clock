package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.DailyTrain;

import java.time.LocalDate;

public interface DailyStopService {
    void generateByDailyTrain(DailyTrain dailyTrain);

    void deleteByDailyTrainId(Long dailyTrainId);
}
