package com.tuberose.clock.user.service;

import com.tuberose.clock.common.response.PageRes;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.response.PassengerRes;

import java.util.List;

public interface PassengerService {
    Passenger save(PassengerReq passengerReq);

    PageRes<PassengerRes> query(Integer pageNum, Integer pageSize);
}
