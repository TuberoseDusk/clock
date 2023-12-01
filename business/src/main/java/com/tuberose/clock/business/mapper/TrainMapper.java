package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Train;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainMapper {
    int insert(Train train);

    Train selectByCode(String code);

    List<Train> select(Train train);

    List<Train> selectAll();

    Train selectByTrainId(Long trainId);

    int delete(Long trainId);
}
