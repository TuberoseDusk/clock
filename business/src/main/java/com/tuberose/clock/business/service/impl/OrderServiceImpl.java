package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.enums.OrderStateEnum;
import com.tuberose.clock.business.enums.RedisKeyEnum;
import com.tuberose.clock.business.enums.TicketStateEnum;
import com.tuberose.clock.business.mapper.DailyCarriageMapper;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.DailyTrainMapper;
import com.tuberose.clock.business.mapper.OrderMapper;
import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.request.TicketSubmissionReq;
import com.tuberose.clock.business.service.DailyTrainService;
import com.tuberose.clock.business.service.OrderService;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.business.service.TicketService;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public void submit(OrderSubmissionReq orderSubmissionReq) {

        String lockKey = RedisKeyEnum.ORDER_CONFIRM.getKeyPrefix() + orderSubmissionReq.getDailyTrainId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean tried = lock.tryLock(3, TimeUnit.SECONDS);
            if (!tried) {
                throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_LOCK_ERROR);
            }

            // 判断是否存在这样的列车
            DailyTrain dailyTrain = dailyTrainService.queryByDailyTrainId(orderSubmissionReq.getDailyTrainId());
            if (dailyTrain == null) {
                throw new BusinessException(ErrorCodeEnum.TRAIN_SECTION_NOT_EXIST);
            }

            // 判断是否存在这样的车票
            DailySection dailySection = sectionService.queryByDailyTrainId(dailyTrain.getDailyTrainId(),
                    orderSubmissionReq.getStartStop(), orderSubmissionReq.getEndStop());
            if (dailySection == null) {
                throw new BusinessException(ErrorCodeEnum.TRAIN_SECTION_NOT_EXIST);
            }

            Long orderId = Snowflake.nextId();
            BigDecimal orderPrice = new BigDecimal("0");
            // 循环买票
            for (TicketSubmissionReq ticketSubmissionReq : orderSubmissionReq.getTicketSubmissionReqs()) {
                // 判断余票数量
                switch (ticketSubmissionReq.getType()) {
                    case 1 -> {
                        if (dailySection.getFirstClassSeatCount() <= 0) {
                            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_OUT);
                        }
                    }
                    case 2 -> {
                        if (dailySection.getSecondClassSeatCount() <= 0) {
                            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_OUT);
                        }
                    }
                }

                // TODO 选座策略
                String pattern = "_".repeat(dailySection.getStartStopIndex())
                        + "0".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex()) + "%";
                List<DailyCarriage> dailyCarriages = dailyCarriageMapper.selectByDailyTrainIdAndType(
                        dailyTrain.getDailyTrainId(), ticketSubmissionReq.getType());

                DailySeat dailySeat = null;
                DailyCarriage dailyCarriage = null;
                for (int i = 0; i < dailyCarriages.size() && dailySeat == null; i++) {
                    dailyCarriage = dailyCarriages.get(i);
                    dailySeat = dailySeatMapper.selectOneByDailyCarriageIdAndColAndState(
                            dailyCarriage.getDailyCarriageId(), ticketSubmissionReq.getType(),
                            ticketSubmissionReq.getCol(), pattern);
                }

                // 选座失败
                if (dailySeat == null) {
                    throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_OUT);
                }

                // 更新座位售卖状态
                String seatState = dailySeat.getState();
                String newSeatState = seatState.substring(0, dailySection.getStartStopIndex())
                        + "1".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex())
                        + seatState.substring(dailySection.getEndStopIndex());
                dailySeatMapper.updateState(dailySeat.getDailySeatId(), newSeatState);

                switch (ticketSubmissionReq.getType()) {
                    case 1 -> dailySection.setFirstClassSeatCount(dailySection.getFirstClassSeatCount() - 1);
                    case 2 -> dailySection.setSecondClassSeatCount(dailySection.getSecondClassSeatCount() - 1);
                }
                sectionService.updateCache(dailySection);

                // 新增车票记录
                Ticket ticket = new Ticket();
                ticket.setTicketId(Snowflake.nextId());
                ticket.setOrderId(orderId);
                ticket.setPassengerId(ticketSubmissionReq.getPassengerId());
                ticket.setTrainCode(dailyTrain.getCode());
                ticket.setCarriageIndex(dailyCarriage.getIndex());
                ticket.setType(dailyCarriage.getType());
                ticket.setRow(dailySeat.getRow());
                ticket.setCol(dailySeat.getCol());
                ticket.setStartStop(dailySection.getStartStop());
                ticket.setStartTime(dailySection.getStartTime());
                ticket.setEndStop(dailySection.getEndStop());
                ticket.setEndTime(dailySection.getEndTime());
                switch (ticket.getType()) {
                    case 1 -> ticket.setPrice(dailySection.getFirstClassSeatPrice());
                    case 2 -> ticket.setPrice(dailySection.getSecondClassSeatPrice());
                }
                ticket.setState(TicketStateEnum.NO_TICKET_ISSUED.getCode());

                orderPrice = orderPrice.add(ticket.getPrice());
                ticketService.save(ticket);
            }

            // 保存订单
            Order order = new Order();
            order.setOrderId(Snowflake.nextId());
            order.setUserId(UserHolder.getUserId());
            order.setTime(LocalDateTime.now());
            order.setPrice(orderPrice);
            order.setState(OrderStateEnum.ORDER_CONFIRMED.getCode());
            orderMapper.insert(order);
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_LOCK_ERROR);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
