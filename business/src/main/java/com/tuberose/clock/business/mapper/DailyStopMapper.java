package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailyStop;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailyStopMapper {
    int deleteByDateAndTrainCode(LocalDate date, String trainCode);

    int insert(DailyStop dailyStop);

    int countByDateAndTrainCode(LocalDate date, String trainCode);
}
