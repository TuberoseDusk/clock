package com.tuberose.clock.business.service;

import com.tuberose.clock.business.request.TicketSubmissionReq;

import java.util.List;

public interface OrderService {
    void submit(List<TicketSubmissionReq> ticketSubmissionReqs);
}