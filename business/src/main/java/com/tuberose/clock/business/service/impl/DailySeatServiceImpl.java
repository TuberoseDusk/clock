package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.*;
import com.tuberose.clock.business.mapper.*;
import com.tuberose.clock.business.service.DailySeatService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DailySeatServiceImpl implements DailySeatService {
    @Resource
    private DailySeatMapper dailySeatMapper;

    @Resource
    private SeatMapper seatMapper;

    @Resource
    private DailyStopMapper dailyStopMapper;

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Override
    @Transactional
    public void generateByDailyCarriage(DailyCarriage dailyCarriage) {
        DailyTrain dailyTrain = dailyTrainMapper.selectByDailyTrainId(dailyCarriage.getDailyTrainId());
        List<Seat> seats = seatMapper.selectByTrainCodeAndCarriageIndex(dailyTrain.getCode(), dailyCarriage.getIndex());
        for (Seat seat : seats) {
            DailySeat dailySeat = new DailySeat();
            dailySeat.setDailySeatId(Snowflake.nextId());
            dailySeat.setDailyCarriageId(dailyCarriage.getDailyCarriageId());
            dailySeat.setRow(seat.getRow());
            dailySeat.setCol(seat.getCol());
            dailySeat.setNumber(seat.getNumber());

            int stopCount = dailyStopMapper.countByDailyTrainId(dailyCarriage.getDailyTrainId());
            dailySeat.setState("0".repeat(stopCount));
            dailySeatMapper.insert(dailySeat);
        }
    }

    @Override
    public void deleteByDailyCarriageId(Long dailyCarriageId) {
        dailySeatMapper.deleteByDailyCarriageId(dailyCarriageId);
    }

    @Override
    public DailySeat queryAvailable(Long dailyCarriageId, String col, String pattern) {
        return dailySeatMapper.selectOneByDailyCarriageIdAndColAndState(dailyCarriageId, col, pattern);
    }

    @Override
    public void updateStateByDailySeatId(Long dailySeatId, String state) {
        dailySeatMapper.updateState(dailySeatId, state);
    }
}
