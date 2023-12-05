package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.DailyCarriage;
import com.tuberose.clock.business.entity.DailySeat;
import com.tuberose.clock.business.entity.Seat;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.mapper.SeatMapper;
import com.tuberose.clock.business.service.DailySeatService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailySeatServiceImpl implements DailySeatService {
    @Resource
    private DailySeatMapper dailySeatMapper;

    @Resource
    private SeatMapper seatMapper;

    @Resource
    private DailyStopMapper dailyStopMapper;

    @Override
    @Transactional
    public void generate(DailyCarriage dailyCarriage) {
        dailySeatMapper.deleteByDateAndTrainCodeAndCarriageIndexInt(dailyCarriage.getDate(),
                dailyCarriage.getTrainCode(), dailyCarriage.getIndex());

        List<Seat> seats = seatMapper.selectByTrainCodeAndCarriageIndex(dailyCarriage.getTrainCode(),
                dailyCarriage.getIndex());
        for (Seat seat : seats) {
            DailySeat dailySeat = BeanUtil.copyProperties(seat, DailySeat.class);
            dailySeat.setDate(dailyCarriage.getDate());
            dailySeat.setDailySeatId(Snowflake.nextId());

            int stopCount = dailyStopMapper.countByDateAndTrainCode(dailyCarriage.getDate(), dailySeat.getTrainCode());
            dailySeat.setState("0".repeat(stopCount));
            dailySeatMapper.insert(dailySeat);
        }
    }

    @Override
    public void deleteAll(LocalDate date) {
        dailySeatMapper.deleteByDate(date);
    }
}
