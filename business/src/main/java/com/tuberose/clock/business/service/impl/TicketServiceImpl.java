package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.mapper.TicketMapper;
import com.tuberose.clock.business.request.OrderSubmissionReq;
import com.tuberose.clock.business.request.TicketSubmissionReq;
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

    public DailySection availableTicketCheck(OrderSubmissionReq orderSubmissionReq) {
        int firstClassSeatRequest = 0, secondClassSeatRequest = 0;
        for (TicketSubmissionReq ticketSubmissionReq : orderSubmissionReq.getTicketSubmissionReqs()) {
            switch (ticketSubmissionReq.getType()) {
                case 1 -> firstClassSeatRequest++;
                case 2 -> secondClassSeatRequest++;
            }
        }
        DailySection dailySection = sectionService.queryByDailyTrainId(orderSubmissionReq.getDailyTrainId(),
                orderSubmissionReq.getStartStop(), orderSubmissionReq.getEndStop());
        if (dailySection == null) {
            throw new BusinessException(ErrorCodeEnum.TRAIN_SECTION_NOT_EXIST);
        }
        if (dailySection.getFirstClassSeatCount() < firstClassSeatRequest
                || dailySection.getSecondClassSeatCount() < secondClassSeatRequest) {
            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_OUT);
        }
        return dailySection;
    }

    @Override
    public void updateByTicketId(Long ticketId, Integer carriageIndex, String row, String col, Integer state) {
        ticketMapper.updateByTicketId(ticketId, carriageIndex, row, col, state);
    }

}
