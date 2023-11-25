package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.Stop;
import com.tuberose.clock.business.mapper.StopMapper;
import com.tuberose.clock.business.request.StopReq;
import com.tuberose.clock.business.response.StopRes;
import com.tuberose.clock.business.service.StopService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Collections;
import java.util.List;

@Service
public class StopServiceImpl implements StopService {

    @Resource
    private StopMapper stopMapper;

    @Override
    @Transactional
    public void save(StopReq stopReq) {
        List<Stop> stops = stopMapper.selectByTrainCodeOrderByIndex(stopReq.getTrainCode());
        if (stops == null || stops.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.TRAIN_CODE_NOT_EXIST);
        }

        Stop firstStop = stops.get(0);
        Stop lastStop = stops.get(stops.size() - 1);

        if (stopReq.getIndex() <= firstStop.getIndex() || stopReq.getIndex() > lastStop.getIndex()) {
            throw new BusinessException(ErrorCodeEnum.ILLEGAL_STOP_INDEX);
        }
        Collections.reverse(stops);
        for (Stop stop : stops) {
            if (stop.getIndex() >= stopReq.getIndex()) {
                stopMapper.updateIndex(stop.getStopId(), stop.getIndex() + 1);
            }
        }


        Stop stop = BeanUtil.copyProperties(stopReq, Stop.class);
        stop.setStopId(Snowflake.nextId());

        stopMapper.insert(stop);
    }

    @Override
    public List<StopRes> queryByTrainCode(String trainCode) {
        List<Stop> stops = stopMapper.selectByTrainCodeOrderByIndex(trainCode);
        return BeanUtil.copyToList(stops, StopRes.class);
    }

    @Override
    @Transactional
    public void delete(Long stopId) {
        Stop stop = stopMapper.selectByStopId(stopId);
        if (stop == null) {
            return;
        }

        List<Stop> stops = stopMapper.selectByTrainCodeOrderByIndex(stop.getTrainCode());
        if (stops.get(0).getStopId().equals(stopId)) {
            throw new BusinessException(ErrorCodeEnum.STOP_IS_START);
        }
        if (stops.get(stops.size() - 1).getStopId().equals(stopId)) {
            throw new BusinessException(ErrorCodeEnum.STOP_IS_END);
        }

        stopMapper.deleteByStopId(stopId);
        for (Stop existStop : stops) {
            if (existStop.getIndex() > stop.getIndex()) {
                stopMapper.updateIndex(existStop.getStopId(), existStop.getIndex() - 1);
            }
        }

    }
}
