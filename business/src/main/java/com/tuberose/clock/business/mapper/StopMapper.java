package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Stop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StopMapper {
    int insert(Stop stop);

    int deleteByStopId(Long stopId);

    int deleteByTrainCode(String trainCode);

    List<Stop> selectByName(String name);

    List<Stop> selectByTrainCodeOrderByIndex(String trainCode);

    Stop selectByTrainCodeAndIndex(String trainCode, Integer index);

    Stop selectByStopId(Long stopId);

    int updateIndex(Long stopId, Integer index);
}
