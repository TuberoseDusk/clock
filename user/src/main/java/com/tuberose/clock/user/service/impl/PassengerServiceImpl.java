package com.tuberose.clock.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.util.Snowflake;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.mapper.PassengerMapper;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.service.PassengerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    PassengerMapper passengerMapper;

    @Override
    public Passenger save(PassengerReq passengerReq) {
        Passenger passenger = BeanUtil.copyProperties(passengerReq, Passenger.class);
        passenger.setPassengerId(Snowflake.nextId());
        passenger.setUserId(UserHolder.getUserId());
        passengerMapper.insert(passenger);
        return passenger;
    }
}
