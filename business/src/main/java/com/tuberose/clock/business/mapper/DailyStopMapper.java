package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.DailyStop;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyStopMapper {
    int deleteByDateAndTrainCode(LocalDate date, String trainCode);

    int insert(DailyStop dailyStop);

    int countByDateAndTrainCode(LocalDate date, String trainCode);

    List<DailySection> selectDailySection(LocalDate date, String startStop, String endStop);
}
