package com.tuberose.clock.user.service;

import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.request.PassengerReq;

public interface PassengerService {
    Passenger save(PassengerReq passengerReq);
}
