package com.tuberose.clock.business.mapper;

import com.tuberose.clock.business.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    Integer insert(Order order);
    Integer updateStateByOrderId(Long orderId, Integer state);

    List<Order> selectByUserIdOrderByTimeDesc(Long userId);
}
