package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.Ticket;
import com.tuberose.clock.business.mapper.TicketMapper;
import com.tuberose.clock.business.service.TicketService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @Resource
    private TicketMapper ticketMapper;

    @Override
    public void save(Ticket ticket) {
        ticketMapper.insert(ticket);
    }
}
