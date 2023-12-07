package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.DailyCarriage;
import com.tuberose.clock.business.entity.DailySeat;


public interface DailySeatService {
    void generateByDailyCarriage(DailyCarriage dailyCarriage);

    void deleteByDailyCarriageId(Long dailyCarriageId);

    DailySeat queryAvailable(Long dailyCarriageId, String col, String pattern);

    void updateStateByDailySeatId(Long dailySeatId, String state);
}
