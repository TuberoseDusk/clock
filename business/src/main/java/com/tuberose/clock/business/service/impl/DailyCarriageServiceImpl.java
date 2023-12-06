package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.Carriage;
import com.tuberose.clock.business.entity.DailyCarriage;
import com.tuberose.clock.business.entity.DailyTrain;
import com.tuberose.clock.business.mapper.CarriageMapper;
import com.tuberose.clock.business.mapper.DailyCarriageMapper;
import com.tuberose.clock.business.service.DailyCarriageService;
import com.tuberose.clock.business.service.DailySeatService;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyCarriageServiceImpl implements DailyCarriageService {

    @Resource
    private DailyCarriageMapper dailyCarriageMapper;

    @Resource
    private CarriageMapper carriageMapper;

    @Resource
    private DailySeatService dailySeatService;

    @Override
    @Transactional
    public void generateByDailyTrain(DailyTrain dailyTrain) {
        dailyCarriageMapper.deleteByDailyTrainId(dailyTrain.getDailyTrainId());
        List<Carriage> carriages = carriageMapper.selectByTrainCodeOrderByIndex(dailyTrain.getCode());
        for (Carriage carriage : carriages) {
            DailyCarriage dailyCarriage = new DailyCarriage(Snowflake.nextId(),
                    dailyTrain.getDailyTrainId(), carriage.getIndex(), carriage.getType());
            dailyCarriageMapper.insert(dailyCarriage);
            dailySeatService.generateByDailyCarriage(dailyCarriage);
        }
    }

    @Override
    public void deleteByDailyTrainId(Long dailyTrainId) {

        List<DailyCarriage> dailyCarriages = dailyCarriageMapper.selectByDailyTrainId(dailyTrainId);
        for (DailyCarriage dailyCarriage : dailyCarriages) {
            dailySeatService.deleteByDailyCarriageId(dailyCarriage.getDailyCarriageId());
        }

        dailyCarriageMapper.deleteByDailyTrainId(dailyTrainId);
    }
}
