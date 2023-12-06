package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailyTrain;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyTrainMapper {
    int insert(DailyTrain dailyTrain);

    int deleteByDateAndCode(LocalDate date, String code);

    int deleteByDate(LocalDate date);

    DailyTrain selectByDailyTrainId(Long dailyTrainId);

    List<DailyTrain> selectByDate(LocalDate date);
}
