package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.DailySeat;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface DailySeatMapper {

    int insert(DailySeat dailySeat);

    int deleteByDateAndTrainCodeAndCarriageIndexInt(LocalDate date, String trainCode, Integer carriageIndex);
}
