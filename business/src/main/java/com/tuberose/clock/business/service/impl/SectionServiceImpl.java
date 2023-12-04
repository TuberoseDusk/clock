package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.enums.CarriageTypeEnum;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.service.SectionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService {
    @Resource
    private RedisTemplate<String, DailySection> redisTemplate;

    @Resource
    private DailyStopMapper dailyStopMapper;

    @Resource
    private DailySeatMapper dailySeatMapper;

    @Override
    public List<DailySection> query(LocalDate date, String startStop, String endStop) {
        List<DailySection> dailySections = dailyStopMapper.selectDailySection(date, startStop, endStop);
        for (DailySection dailySection : dailySections) {
            querySectionSeatCount(dailySection);
        }
        return dailySections;
    }

    @Override
    public DailySection queryByDateAndTrainCode(LocalDate date, String trainCode, String startStop, String endStop) {
        DailySection dailySection = dailyStopMapper.selectDailySectionByTrainCode(date, trainCode, startStop, endStop);
        querySectionSeatCount(dailySection);
        return dailySection;
    }

    private void querySectionSeatCount(DailySection dailySection) {
        String key = dailySection.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE)
                + " " + dailySection.getTrainCode()
                + ":" + dailySection.getStartStop() + "-" + dailySection.getEndStop();
        DailySection cachedDailySection = redisTemplate.opsForValue().get(key);
        if (cachedDailySection != null) {
            BeanUtil.copyProperties(cachedDailySection, dailySection);
            log.info("DailySection Cache HIT");
            return;
        }

        String pattern = "_".repeat(dailySection.getStartStopIndex())
                + "0".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex()) + "%";
        Integer firstClassSeatCount = dailySeatMapper.countByDateAndTrainCodeAndTypeAndState(dailySection.getDate(),
                dailySection.getTrainCode(), CarriageTypeEnum.FIRST_CLASS_CARRIAGE.getCode(), pattern);
        Integer secondClassSeatCount = dailySeatMapper.countByDateAndTrainCodeAndTypeAndState(dailySection.getDate(),
                dailySection.getTrainCode(), CarriageTypeEnum.SECOND_CLASS_CARRIAGE.getCode(), pattern);
        dailySection.setFirstClassSeatCount(firstClassSeatCount);
        dailySection.setSecondClassSeatCount(secondClassSeatCount);

        // TODO 定价策略
        dailySection.setFirstClassSeatPrice(new BigDecimal("500"));
        dailySection.setSecondClassSeatPrice(new BigDecimal("200"));

        redisTemplate.opsForValue().set(key, dailySection, 5, TimeUnit.MINUTES);
    }
}
