package com.tuberose.clock.business.controller;

import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.request.TicketSubmissionReq;
import com.tuberose.clock.business.service.OrderService;
import com.tuberose.clock.common.response.BaseRes;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/submit")
    public BaseRes<Void> submit(@Valid @RequestBody OrderSubmissionReq orderSubmissionReq) {
        orderService.submit(orderSubmissionReq);
        return BaseRes.success();
    }
}
