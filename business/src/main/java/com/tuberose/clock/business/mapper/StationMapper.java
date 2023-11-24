package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {

    Station selectByStationId(Long stationId);

    Station selectByName(String name);

    Station selectByAbbrev(String abbrev);

    int insert(Station station);

    int deleteByStationId(Long stationId);

    List<Station> query(Station station);

}
