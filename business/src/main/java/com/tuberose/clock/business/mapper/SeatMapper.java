package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Seat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeatMapper {
    int insert(Seat seat);

    List<Seat> selectByTrainCodeAndCarriageIndex(String trainCode, Integer carriageIndex);

    int updateIndexByTrainCodeAndCarriageIndex(String trainCode, Integer carriageIndex, Integer newCarriageIndex);
}
