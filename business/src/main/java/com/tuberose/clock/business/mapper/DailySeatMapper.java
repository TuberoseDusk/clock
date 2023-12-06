package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailySeat;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailySeatMapper {

    int insert(DailySeat dailySeat);

    int deleteByDailyCarriageId(Long dailyCarriageId);

    int countByDailyTrainIdAndTypeAndState(Long dailyTrainId, Integer type, String pattern);

    DailySeat selectOneByDailyCarriageIdAndColAndState(Long dailyCarriageId, Integer type, String col, String pattern);

    Integer updateState(Long dailySeatId, String state);
}
