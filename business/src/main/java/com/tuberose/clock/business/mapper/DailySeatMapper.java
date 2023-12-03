package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailySeat;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailySeatMapper {

    int insert(DailySeat dailySeat);

    int deleteByDateAndTrainCodeAndCarriageIndexInt(LocalDate date, String trainCode, Integer carriageIndex);

    int countByDateAndTrainCodeAndTypeAndState(LocalDate date, String trainCode, Integer type, String pattern);

    DailySeat selectOneByDateAndTrainCodeAndTypeAndColAndState(LocalDate date, String trainCode,
                                                               Integer type, String col, String pattern);

    Integer updateState(Long dailySeatId, String state);
}
