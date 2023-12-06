package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.DailyStop;
import com.tuberose.clock.business.entity.DailyTrain;
import com.tuberose.clock.business.entity.Stop;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.mapper.StopMapper;
import com.tuberose.clock.business.service.DailyStopService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class DailyStopServiceImpl implements DailyStopService {

    @Resource
    private DailyStopMapper dailyStopMapper;

    @Resource
    private StopMapper stopMapper;

    @Override
    @Transactional
    public void generateByDailyTrain(DailyTrain dailyTrain) {
        List<Stop> stops = stopMapper.selectByTrainCodeOrderByIndex(dailyTrain.getCode());
        for (Stop stop : stops) {
            DailyStop dailyStop = new DailyStop();
            dailyStop.setDailyStopId(Snowflake.nextId());
            dailyStop.setDailyTrainId(dailyTrain.getDailyTrainId());
            dailyStop.setIndex(stop.getIndex());
            dailyStop.setName(stop.getName());

            long timeStamp = dailyTrain.getDate().toEpochSecond(LocalTime.of(0, 0, 0),
                    ZoneOffset.UTC);
            LocalDateTime arrivalTime = LocalDateTime.ofEpochSecond(timeStamp + stop.getArrivalTime(),
                    0, ZoneOffset.UTC);
            LocalDateTime departureTime = LocalDateTime.ofEpochSecond(timeStamp + stop.getDepartureTime(),
                    0, ZoneOffset.UTC);
            dailyStop.setArrivalTime(arrivalTime);
            dailyStop.setDepartureTime(departureTime);

            dailyStopMapper.insert(dailyStop);
        }
    }

    @Override
    public void deleteByDailyTrainId(Long dailyTrainId) {
        dailyStopMapper.deleteByDailyTrainId(dailyTrainId);
    }
}
