package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.Order;
import com.tuberose.clock.business.entity.Ticket;
import com.tuberose.clock.business.enums.OrderStateEnum;
import com.tuberose.clock.business.enums.RedisKeyEnum;
import com.tuberose.clock.business.enums.TicketStateEnum;
import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.request.TicketSubmissionReq;
import com.tuberose.clock.business.service.*;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private OrderService orderService;

    @Resource
    private TicketService ticketService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void confirmOrder(OrderSubmissionReq orderSubmissionReq) {
        // 是否存在这样的线路
        DailySection dailySection = ticketService.availableTicketCheck(orderSubmissionReq);

        // 生成订单
        Order order = new Order();
        order.setOrderId(Snowflake.nextId());
        order.setUserId(UserHolder.getUserId());
        order.setTime(LocalDateTime.now());
        order.setPrice(new BigDecimal("0"));
        order.setState(OrderStateEnum.ORDER_SUBMITTED.getCode());
        // 生成车票
        List<Ticket> tickets = new ArrayList<>(orderSubmissionReq.getTicketSubmissionReqs().size());
        for (TicketSubmissionReq ticketSubmissionReq : orderSubmissionReq.getTicketSubmissionReqs()) {
            Ticket ticket = new Ticket();
            ticket.setTicketId(Snowflake.nextId());
            ticket.setOrderId(order.getOrderId());
            ticket.setPassengerId(ticketSubmissionReq.getPassengerId());
            ticket.setTrainCode(dailySection.getTrainCode());
            ticket.setCarriageIndex(null);
            ticket.setType(ticketSubmissionReq.getType());
            ticket.setRow(null);
            ticket.setCol(ticketSubmissionReq.getCol());    // 预选位置
            ticket.setStartStop(orderSubmissionReq.getStartStop());
            ticket.setStartTime(dailySection.getStartTime());
            ticket.setEndStop(orderSubmissionReq.getEndStop());
            ticket.setEndTime(dailySection.getEndTime());
            switch (ticket.getType()) {
                case 1 -> ticket.setPrice(dailySection.getFirstClassSeatPrice());
                case 2 -> ticket.setPrice(dailySection.getSecondClassSeatPrice());
            }
            order.setPrice(order.getPrice().add(ticket.getPrice()));
            ticket.setState(TicketStateEnum.NO_TICKET_ISSUED.getCode());
            tickets.add(ticket);
        }

        // 保存车票和订单
        orderService.save(order, tickets);

        String lockKey = RedisKeyEnum.ORDER_CONFIRM.getKeyPrefix() + orderSubmissionReq.getDailyTrainId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean tried = lock.tryLock(30, TimeUnit.SECONDS);
            if (!tried) {
                throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_LOCK_ERROR);
            }
            // 选座购票
            orderService.generateTickets(tickets, order, orderSubmissionReq);

        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_LOCK_ERROR);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
