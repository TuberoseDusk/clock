package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.mapper.DailyCarriageMapper;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.OrderMapper;
import com.tuberose.clock.business.service.DailyTrainService;
import com.tuberose.clock.business.service.OrderService;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.business.service.TicketService;
import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DailySeatMapper dailySeatMapper;

    @Resource
    private DailyTrainService dailyTrainService;

    @Resource
    private DailyCarriageMapper dailyCarriageMapper;

    @Resource
    private SectionService sectionService;

    @Resource
    private TicketService ticketService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public void save(Order order, List<Ticket> tickets) {
        orderMapper.insert(order);
        ticketService.saveTickets(tickets);
    }

    @Override
    public void updateStateByOrderId(Long orderId, Integer state) {
        orderMapper.updateStateByOrderId(orderId, state);
    }
}
