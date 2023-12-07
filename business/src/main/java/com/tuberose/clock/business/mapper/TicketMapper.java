package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketMapper {
    Integer insert(Ticket ticket);
    Integer updateStateByTicketId(Long ticketId, Integer state);

    List<Ticket> selectByOrderIdOrderByStartTimeDesc(Long orderId);

    List<Ticket> selectByPassengerIdOrderByStartTimeDesc(Long passengerId);

    Integer updateByTicketId(Long ticketId, Integer carriageIndex, String row, String col, Integer state);
}
