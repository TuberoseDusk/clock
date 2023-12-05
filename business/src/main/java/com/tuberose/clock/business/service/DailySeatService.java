package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.DailyCarriage;

import java.time.LocalDate;

public interface DailySeatService {
    void generate(DailyCarriage dailyCarriage);

    void deleteAll(LocalDate date);
}
