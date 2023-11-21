package com.tuberose.clock.user.mapper;

import com.tuberose.clock.user.entity.Passenger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PassengerMapper {
    int insert(Passenger passenger);

    List<Passenger> selectByUserId(Long userId);

    Passenger selectByPassengerId(Long passengerId);

    List<Passenger> select(Passenger passenger);
}
