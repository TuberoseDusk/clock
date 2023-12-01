package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.DailyStop;
import com.tuberose.clock.business.entity.Stop;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.mapper.StopMapper;
import com.tuberose.clock.business.service.DailyStopService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public void generate(LocalDate date, String trainCode) {
        dailyStopMapper.deleteByDateAndTrainCode(date, trainCode);
        List<Stop> stops = stopMapper.selectByTrainCodeOrderByIndex(trainCode);
        for (Stop stop : stops) {
            DailyStop dailyStop = BeanUtil.copyProperties(stop, DailyStop.class,
                    "arrivalTime", "departureTime");
            dailyStop.setDailyStopId(Snowflake.nextId());
            dailyStop.setDate(date);

            long timeStamp = date.toEpochSecond(LocalTime.of(0, 0, 0), ZoneOffset.UTC);
            LocalDateTime arrivalTime = LocalDateTime.ofEpochSecond(timeStamp + stop.getArrivalTime(),
                    0, ZoneOffset.UTC);
            LocalDateTime departureTime = LocalDateTime.ofEpochSecond(timeStamp + stop.getDepartureTime(),
                    0, ZoneOffset.UTC);
            dailyStop.setArrivalTime(arrivalTime);
            dailyStop.setDepartureTime(departureTime);

            dailyStopMapper.insert(dailyStop);
        }
    }
}
