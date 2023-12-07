package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.enums.OrderStateEnum;
import com.tuberose.clock.business.enums.TicketStateEnum;
import com.tuberose.clock.business.mapper.DailyCarriageMapper;
import com.tuberose.clock.business.mapper.OrderMapper;
import com.tuberose.clock.business.mapper.TicketMapper;
import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.service.DailySeatService;
import com.tuberose.clock.business.service.OrderService;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.business.service.TicketService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Resource
    private TicketMapper ticketMapper;

    @Resource
    private SectionService sectionService;

    @Resource
    private DailyCarriageMapper dailyCarriageMapper;

    @Resource
    private DailySeatService dailySeatService;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void save(Ticket ticket) {
        ticketMapper.insert(ticket);
    }

    @Override
    @Transactional
    public void saveTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            save(ticket);
        }
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

            // 扣缓存
            switch (ticket.getType()) {
                case 1 -> dailySection.setFirstClassSeatCount(dailySection.getFirstClassSeatCount() - 1);
                case 2 -> dailySection.setSecondClassSeatCount(dailySection.getSecondClassSeatCount() - 1);
            }
            sectionService.updateCache(dailySection);

            // 更新座位售卖状态
            String seatState = dailySeat.getState();
            String newSeatState = seatState.substring(0, dailySection.getStartStopIndex())
                    + "1".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex())
                    + seatState.substring(dailySection.getEndStopIndex());
            dailySeatService.updateStateByDailySeatId(dailySeat.getDailySeatId(), newSeatState);

            // 更新车票记录
            ticket.setCarriageIndex(dailyCarriage.getIndex());
            ticket.setRow(dailySeat.getRow());
            ticket.setCol(dailySeat.getCol());
            ticket.setState(TicketStateEnum.TICKET_NOT_USED.getCode());
            ticketMapper.updateByTicketId(ticket.getTicketId(), ticket.getCarriageIndex(),
                    ticket.getRow(), ticket.getCol(), ticket.getState());
        }

        // 更新订单状态
        orderMapper.updateStateByOrderId(order.getOrderId(), OrderStateEnum.ORDER_CONFIRMED.getCode());
    }

}
