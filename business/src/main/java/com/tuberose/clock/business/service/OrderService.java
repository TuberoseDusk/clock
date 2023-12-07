package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.Order;
import com.tuberose.clock.business.entity.Ticket;

import java.util.List;

public interface OrderService {
    void save(Order order, List<Ticket> tickets);

    void updateStateByOrderId(Long orderId, Integer state);
}
