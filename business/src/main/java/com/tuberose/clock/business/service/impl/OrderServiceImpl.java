package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.DailySeat;
import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.Order;
import com.tuberose.clock.business.entity.Ticket;
import com.tuberose.clock.business.enums.OrderStateEnum;
import com.tuberose.clock.business.enums.TicketStateEnum;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.OrderMapper;
import com.tuberose.clock.business.request.TicketSubmissionReq;
import com.tuberose.clock.business.service.OrderService;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.business.service.TicketService;
import com.tuberose.clock.common.context.UserHolder;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DailySeatMapper dailySeatMapper;

    @Resource
    private SectionService sectionService;

    @Resource
    private TicketService ticketService;

    @Override
    @Transactional
    public void submit(List<TicketSubmissionReq> ticketSubmissionReqs) {
        // TODO 超卖
        Long orderId = Snowflake.nextId();
        BigDecimal orderPrice = new BigDecimal("0");
        for (TicketSubmissionReq ticketSubmissionReq : ticketSubmissionReqs) {

            // 判断是否存在这样的车票
            LocalDate date = ticketSubmissionReq.getStartTime().toLocalDate();
            DailySection dailySection = sectionService.queryByDateAndTrainCode(date, ticketSubmissionReq.getTrainCode(),
                    ticketSubmissionReq.getStartStop(), ticketSubmissionReq.getEndStop());
            if (dailySection == null) {
                throw new BusinessException(ErrorCodeEnum.TRAIN_SECTION_NOT_EXIST);
            }

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
            DailySeat dailySeat = dailySeatMapper.selectOneByDateAndTrainCodeAndTypeAndColAndState(
                    dailySection.getDate(), dailySection.getTrainCode(), ticketSubmissionReq.getType(),
                    ticketSubmissionReq.getCol(), pattern);

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

            // 新增购票记录
            Ticket ticket = new Ticket();
            ticket.setTicketId(Snowflake.nextId());
            ticket.setOrderId(orderId);
            ticket.setPassengerId(ticketSubmissionReq.getPassengerId());
            ticket.setTrainCode(dailySeat.getTrainCode());
            ticket.setCarriageIndex(dailySeat.getCarriageIndex());
            ticket.setType(dailySeat.getType());
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
    }
}