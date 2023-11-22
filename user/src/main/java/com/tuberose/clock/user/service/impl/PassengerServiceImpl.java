package com.tuberose.clock.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.response.PageRes;
import com.tuberose.clock.common.util.Snowflake;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.mapper.PassengerMapper;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.response.PassengerRes;
import com.tuberose.clock.user.service.PassengerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    PassengerMapper passengerMapper;

    @Override
    public Passenger save(PassengerReq passengerReq) {
        Passenger passenger = BeanUtil.copyProperties(passengerReq, Passenger.class);
        List<Passenger> existPassengers = passengerMapper.select(passenger);
        if (existPassengers != null && !existPassengers.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.PASSENGER_EXIST);
        }

        passenger.setPassengerId(Snowflake.nextId());
        passenger.setUserId(UserHolder.getUserId());
        passengerMapper.insert(passenger);
        return passenger;
    }

    @Override
    public PageRes<PassengerRes> query(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Passenger> passengers = passengerMapper.selectByUserId(UserHolder.getUserId());

        PageInfo<Passenger> passengerPageInfo = new PageInfo<>(passengers);
        List<PassengerRes> passengerResList = BeanUtil.copyToList(passengers, PassengerRes.class);
        return PageRes.of(pageNum, pageSize, passengerPageInfo.getTotal(), passengerResList);
    }

    @Override
    public void delete(Long passengerId) {
        Passenger passenger = new Passenger();
        passenger.setUserId(UserHolder.getUserId());
        passenger.setPassengerId(passengerId);

        List<Passenger> existPassengers = passengerMapper.select(passenger);
        if (existPassengers == null || existPassengers.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.PASSENGER_NOT_EXIST);
        }
        passengerMapper.deleteByPassengerId(passengerId);
    }

}
