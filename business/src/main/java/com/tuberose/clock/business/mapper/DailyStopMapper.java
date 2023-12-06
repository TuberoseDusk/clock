package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.DailyStop;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyStopMapper {
    int deleteByDateAndTrainCode(LocalDate date, String trainCode);

    int deleteByDailyTrainId(Long dailyTrainId);

    int insert(DailyStop dailyStop);

    int countByDailyTrainId(Long dailyTrainId);

    // 按照实际出发日期查询，而非发车日期
    List<DailySection> selectDailySection(LocalDate date, String startStop, String endStop);

    DailySection selectDailySectionByDailyTrainId(Long dailyTrainId, String startStop, String endStop);
}
