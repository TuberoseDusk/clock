package com.tuberose.clock.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.mapper.PassengerMapper;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.service.PassengerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}
