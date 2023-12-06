package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailyCarriage;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyCarriageMapper {
    Integer deleteByDailyTrainId(Long dailyTrainId);

    Integer insert(DailyCarriage dailyCarriage);

    List<DailyCarriage> selectByDailyTrainIdAndType(Long dailyTrainId, Integer type);

    List<DailyCarriage> selectByDailyTrainId(Long dailyTrainId);
}
