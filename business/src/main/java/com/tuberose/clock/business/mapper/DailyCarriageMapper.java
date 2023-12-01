package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailyCarriage;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailyCarriageMapper {
    int deleteByDateAndTrainCode(LocalDate date, String TrainCode);

    int insert(DailyCarriage dailyCarriage);
}
