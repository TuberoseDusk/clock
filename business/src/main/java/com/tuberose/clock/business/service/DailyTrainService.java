package com.tuberose.clock.business.service;


import com.tuberose.clock.business.entity.DailyTrain;
import com.tuberose.clock.business.entity.Train;

import java.time.LocalDate;

public interface DailyTrainService {
    void generate(LocalDate date, Long trainId);

    void generateAll(LocalDate date);

    void deleteAll(LocalDate date);

    DailyTrain queryByDailyTrainId(Long dailyTrainId);
}
