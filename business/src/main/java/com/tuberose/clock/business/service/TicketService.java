package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.Ticket;
import com.tuberose.clock.business.request.OrderSubmissionReq;

import java.util.List;

public interface TicketService {
    void save(Ticket ticket);

    void saveTickets(List<Ticket> tickets);

    DailySection availableTicketCheck(OrderSubmissionReq orderSubmissionReq);

    void updateByTicketId(Long ticketId, Integer carriageIndex, String row, String col, Integer state);

}
