package com.tuberose.clock.business.service.impl;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.enums.CarriageTypeEnum;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.service.SectionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    @Resource
    private DailyStopMapper dailyStopMapper;

    @Resource
    private DailySeatMapper dailySeatMapper;

    @Override
    public List<DailySection> query(LocalDate date, String startStop, String endStop) {
        List<DailySection> dailySections = dailyStopMapper.selectDailySection(date, startStop, endStop);
        for (DailySection dailySection : dailySections) {
            String pattern = "_".repeat(dailySection.getStartStopIndex())
                    + "0".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex()) + "%";
            Integer firstClassSeatCount = dailySeatMapper.countByDateAndTrainCodeAndTypeAndState(date,
                    dailySection.getTrainCode(), CarriageTypeEnum.FIRST_CLASS_CARRIAGE.getCode(), pattern);
            Integer secondClassSeatCount = dailySeatMapper.countByDateAndTrainCodeAndTypeAndState(date,
                    dailySection.getTrainCode(), CarriageTypeEnum.SECOND_CLASS_CARRIAGE.getCode(), pattern);

            dailySection.setFirstClassSeatCount(firstClassSeatCount);
            dailySection.setSecondClassSeatCount(secondClassSeatCount);
        }
        return dailySections;
    }
}
