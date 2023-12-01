package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.Carriage;
import com.tuberose.clock.business.entity.DailyCarriage;
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
    public void generate(LocalDate date, String trainCode) {
        dailyCarriageMapper.deleteByDateAndTrainCode(date, trainCode);
        List<Carriage> carriages = carriageMapper.selectByTrainCodeOrderByIndex(trainCode);

        for (Carriage carriage : carriages) {
            DailyCarriage dailyCarriage = BeanUtil.copyProperties(carriage, DailyCarriage.class);
            dailyCarriage.setDate(date);
            dailyCarriage.setDailyCarriageId(Snowflake.nextId());
            dailyCarriageMapper.insert(dailyCarriage);

            dailySeatService.generate(dailyCarriage);
        }
    }
}
