package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuberose.clock.business.entity.Station;
import com.tuberose.clock.business.mapper.StationMapper;
import com.tuberose.clock.business.request.StationReq;
import com.tuberose.clock.business.response.StationRes;
import com.tuberose.clock.business.service.StationService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.response.PageRes;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StationServiceImpl implements StationService {

    @Resource
    private StationMapper stationMapper;

    @Override
    public void save(StationReq stationReq) {
        Station station = stationMapper.selectByName(stationReq.getName());
        if (station != null) {
            throw new BusinessException(ErrorCodeEnum.STATION_NAME_EXIST);
        }
        log.info("name check pass.");
        station = stationMapper.selectByAbbrev(stationReq.getAbbrev());
        if (station != null) {
            throw new BusinessException(ErrorCodeEnum.STATION_ABBREV_EXIST);
        }

        station = BeanUtil.copyProperties(stationReq, Station.class);
        station.setStationId(Snowflake.nextId());
        stationMapper.insert(station);
    }

    @Override
    public void delete(Long stationId) {
        stationMapper.deleteByStationId(stationId);
    }

    @Override
    public PageRes<StationRes> query(StationReq stationReq, Integer pageNum, Integer pageSize) {
        Station station = BeanUtil.copyProperties(stationReq, Station.class);
        PageHelper.startPage(pageNum, pageSize);
        List<Station> stations = stationMapper.query(station);

        PageInfo<Station> stationPageInfo = new PageInfo<>(stations);
        List<StationRes> stationResList = BeanUtil.copyToList(stations, StationRes.class);
        return PageRes.of(pageNum, pageSize, stationPageInfo.getTotal(), stationResList);
    }
}
