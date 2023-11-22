package com.tuberose.clock.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.common.response.PageRes;
import com.tuberose.clock.user.entity.Passenger;
import com.tuberose.clock.user.request.PassengerReq;
import com.tuberose.clock.user.response.PassengerRes;
import com.tuberose.clock.user.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/query/{pageNum}/{pageSize}")
    public PageRes<PassengerRes> query(@PathVariable Integer pageNum,
                                       @PathVariable Integer pageSize) {
        return passengerService.query(pageNum, pageSize);
    }
}
