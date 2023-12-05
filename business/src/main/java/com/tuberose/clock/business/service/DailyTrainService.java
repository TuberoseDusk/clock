package com.tuberose.clock.business.service;


import com.tuberose.clock.business.entity.Train;

import java.time.LocalDate;

public interface DailyTrainService {
    void generate(LocalDate date, Train train);

    void generateAll(LocalDate date);

    void deleteAll(LocalDate date);
}
