package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuberose.clock.business.entity.Station;
import com.tuberose.clock.business.entity.Train;
import com.tuberose.clock.business.mapper.StationMapper;
import com.tuberose.clock.business.mapper.TrainMapper;
import com.tuberose.clock.business.request.TrainReq;
import com.tuberose.clock.business.response.TrainRes;
import com.tuberose.clock.business.service.TrainService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.response.PageRes;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {
    @Resource
    TrainMapper trainMapper;

    @Resource
    StationMapper stationMapper;

    @Override
    public void save(TrainReq trainReq) {
        Train train = trainMapper.selectByCode(trainReq.getCode());
        if (train != null) {
            throw new BusinessException(ErrorCodeEnum.TRAIN_CODE_EXIST);
        }

        train = BeanUtil.copyProperties(trainReq, Train.class);

        Station startStation = stationMapper.selectByName(trainReq.getStartStation());
        if (startStation == null) {
            throw new BusinessException(ErrorCodeEnum.STATION_NOT_EXIST);
        }
        train.setStartStationAbbrev(startStation.getAbbrev());

        Station endStation = stationMapper.selectByName(trainReq.getEndStation());
        if (endStation == null) {
            throw new BusinessException(ErrorCodeEnum.STATION_NOT_EXIST);
        }
        train.setEndStationAbbrev(endStation.getAbbrev());

        train.setTrainId(Snowflake.nextId());
        trainMapper.insert(train);
    }

    @Override
    public PageRes<TrainRes> query(TrainReq trainReq, Integer pageNum, Integer pageSize) {
        Train train = BeanUtil.copyProperties(trainReq, Train.class);

        PageHelper.startPage(pageNum, pageSize);
        List<Train> trains = trainMapper.select(train);
        PageInfo<Train> trainPageInfo = new PageInfo<>(trains);

        return PageRes.of(pageNum, pageSize, trainPageInfo.getTotal(), BeanUtil.copyToList(trains, TrainRes.class));
    }
}
