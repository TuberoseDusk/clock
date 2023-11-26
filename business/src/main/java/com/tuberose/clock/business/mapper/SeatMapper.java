package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Seat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeatMapper {
    int insert(Seat seat);
}
