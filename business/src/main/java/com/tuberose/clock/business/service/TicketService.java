package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.Order;
import com.tuberose.clock.business.entity.Ticket;
import com.tuberose.clock.business.request.OrderSubmissionReq;

import java.util.List;

public interface TicketService {
    void save(Ticket ticket);

    void saveTickets(List<Ticket> tickets);

    void generateTickets(List<Ticket> tickets, Order order, OrderSubmissionReq orderSubmissionReq);

}
