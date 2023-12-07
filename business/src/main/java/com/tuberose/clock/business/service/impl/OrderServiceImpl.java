package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.enums.OrderStateEnum;
import com.tuberose.clock.business.enums.TicketStateEnum;
import com.tuberose.clock.business.mapper.DailyCarriageMapper;
import com.tuberose.clock.business.mapper.OrderMapper;
import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.service.*;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DailyCarriageMapper dailyCarriageMapper;

    @Resource
    private SectionService sectionService;

    @Resource
    private TicketService ticketService;

    @Resource
    private DailySeatService dailySeatService;

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

    @Transactional
    @Override
    public void generateTickets(List<Ticket> tickets, Order order, OrderSubmissionReq orderSubmissionReq) {
        DailySection dailySection = sectionService.queryByDailyTrainId(orderSubmissionReq.getDailyTrainId(),
                orderSubmissionReq.getStartStop(), orderSubmissionReq.getEndStop());
        for (Ticket ticket : tickets) {
            // 判断余票数量
            switch (ticket.getType()) {
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

            // 选座策略
            String pattern = "_".repeat(dailySection.getStartStopIndex())
                    + "0".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex()) + "%";
            List<DailyCarriage> dailyCarriages = dailyCarriageMapper.selectByDailyTrainIdAndType(
                    orderSubmissionReq.getDailyTrainId(), ticket.getType());

            DailySeat dailySeat = null;
            DailyCarriage dailyCarriage = null;
            for (int i = 0; i < dailyCarriages.size() && dailySeat == null; i++) {
                dailyCarriage = dailyCarriages.get(i);
                dailySeat = dailySeatService.queryAvailable(dailyCarriage.getDailyCarriageId(),
                        ticket.getCol(), pattern);
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
            dailySeatService.updateStateByDailySeatId(dailySeat.getDailySeatId(), newSeatState);

            // 扣缓存
            switch (ticket.getType()) {
                case 1 -> dailySection.setFirstClassSeatCount(dailySection.getFirstClassSeatCount() - 1);
                case 2 -> dailySection.setSecondClassSeatCount(dailySection.getSecondClassSeatCount() - 1);
            }
            sectionService.updateCache(dailySection);

            // 更新车票记录
            ticket.setCarriageIndex(dailyCarriage.getIndex());
            ticket.setRow(dailySeat.getRow());
            ticket.setCol(dailySeat.getCol());
            ticket.setState(TicketStateEnum.TICKET_NOT_USED.getCode());
            ticketService.updateByTicketId(ticket.getTicketId(), ticket.getCarriageIndex(),
                    ticket.getRow(), ticket.getCol(), ticket.getState());
        }

        // 更新订单状态
        orderMapper.updateStateByOrderId(order.getOrderId(), OrderStateEnum.ORDER_CONFIRMED.getCode());
    }
}
