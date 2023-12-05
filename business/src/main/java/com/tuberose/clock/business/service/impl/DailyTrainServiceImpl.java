package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.DailyTrain;
import com.tuberose.clock.business.entity.Train;
import com.tuberose.clock.business.mapper.DailyTrainMapper;
import com.tuberose.clock.business.mapper.TrainMapper;
import com.tuberose.clock.business.service.DailyCarriageService;
import com.tuberose.clock.business.service.DailyStopService;
import com.tuberose.clock.business.service.DailyTrainService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;


@Service
@Slf4j
public class DailyTrainServiceImpl implements DailyTrainService {
    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainMapper trainMapper;

    @Resource
    private DailyStopService dailyStopService;

    @Resource
    private DailyCarriageService dailyCarriageService;

    @Override
    @Transactional
    public void generate(LocalDate date, Train train) {
        dailyTrainMapper.deleteByDateAndCode(date, train.getCode());

        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class, "startTime", "endTime");
        dailyTrain.setDailyTrainId(Snowflake.nextId());
        dailyTrain.setDate(date);

        long timeStamp = date.toEpochSecond(LocalTime.of(0, 0, 0), ZoneOffset.UTC);
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(timeStamp + train.getStartTime(), 0, ZoneOffset.UTC);
        LocalDateTime endTime = LocalDateTime.ofEpochSecond(timeStamp + train.getEndTime(), 0, ZoneOffset.UTC);
        dailyTrain.setStartTime(startTime);
        dailyTrain.setEndTime(endTime);

        dailyTrainMapper.insert(dailyTrain);

        dailyStopService.generate(date, train.getCode());
        dailyCarriageService.generate(date, train.getCode());
    }

    @Override
    @Transactional
    public void generateAll(LocalDate date) {
        dailyTrainMapper.deleteByDate(date);
        List<Train> trains = trainMapper.selectAll();
        for (Train train : trains) {
            generate(date, train);
        }
    }

    @Override
    public void deleteAll(LocalDate date) {
        dailyTrainMapper.deleteByDate(date);
        dailyStopService.deleteAll(date);
        dailyCarriageService.deleteAll(date);
    }
}
