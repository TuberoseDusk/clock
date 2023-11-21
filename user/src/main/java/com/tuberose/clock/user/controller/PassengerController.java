package com.tuberose.clock.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.response.PassengerRes;
import com.tuberose.clock.user.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    PassengerService passengerService;

    @PostMapping("/save")
    public BaseRes<PassengerRes> save(@Valid @RequestBody PassengerReq passengerReq) {
        Passenger passenger = passengerService.save(passengerReq);
        PassengerRes passengerRes = BeanUtil.copyProperties(passenger, PassengerRes.class);
        return BaseRes.success(passengerRes);
    }
}
