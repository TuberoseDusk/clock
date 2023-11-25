package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Carriage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarriageMapper {
    int insert(Carriage carriage);
    int delete(Long carriageId);
    List<Carriage> selectByTrainCodeOrderByIndex(String trainCode);
    Carriage selectByTrainCodeAndIndex(String trainCode, Integer index);
    Carriage selectByCarriageId(Long carriageId);
    int updateIndex(Long carriageId, Integer index);
}
