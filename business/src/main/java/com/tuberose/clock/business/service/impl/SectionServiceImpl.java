package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.entity.DailyTrain;
import com.tuberose.clock.business.enums.CarriageTypeEnum;
import com.tuberose.clock.business.enums.RedisKeyEnum;
import com.tuberose.clock.business.mapper.DailySeatMapper;
import com.tuberose.clock.business.mapper.DailyStopMapper;
import com.tuberose.clock.business.mapper.DailyTrainMapper;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
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

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Override
    public List<DailySection> query(LocalDate date, String startStop, String endStop) {
        List<DailySection> dailySections = dailyStopMapper.selectDailySection(date, startStop, endStop);
        for (DailySection dailySection : dailySections) {
            fillSectionSeat(dailySection);
        }
        return dailySections;
    }

    @Override
    public DailySection queryByDailyTrainId(Long dailyTrainId, String startStop, String endStop) {
        String key = RedisKeyEnum.DAILY_SECTION_CACHE.getKeyPrefix() + dailyTrainId + ":"
                + startStop + "-" + endStop;
        DailySection dailySection = redisTemplate.opsForValue().get(key);
        if (dailySection != null) {
            log.info("DailySection Cache HIT " + key + ": {}", dailySection);
            return dailySection;
        }

        dailySection = dailyStopMapper.selectDailySectionByDailyTrainId(dailyTrainId, startStop, endStop);
        if (dailySection != null) {
            fillSectionSeat(dailySection);
        }
        return dailySection;
    }

    @Override
    public void clearCache(Long dailyTrainId, String startStop, String endStop) {
        String key = RedisKeyEnum.DAILY_SECTION_CACHE.getKeyPrefix()
                + dailyTrainId + ":" + startStop + "-" + endStop;
        redisTemplate.delete(key);
    }

    @Override
    public void updateCache(DailySection dailySection) {
        String key = RedisKeyEnum.DAILY_SECTION_CACHE.getKeyPrefix()
                + dailySection.getDailyTrainId()
                + ":" + dailySection.getStartStop() + "-" + dailySection.getEndStop();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().set(key, dailySection);
            log.info("DailySection Cache UPDATE" + key + ": {}", dailySection);
        }
    }

    @Override
    public DailySection checkSectionAvailable(Long dailyTrainId, String startStop, String endStop) {
        // 判断是否存在这样的线路
        DailySection dailySection = queryByDailyTrainId(dailyTrainId, startStop, endStop);
        if (dailySection == null) {
            throw new BusinessException(ErrorCodeEnum.TRAIN_SECTION_NOT_EXIST);
        }

        // 判断该线路是否还有余票
        if (dailySection.getFirstClassSeatCount() <= 0 && dailySection.getSecondClassSeatCount() <= 0) {
            throw new BusinessException(ErrorCodeEnum.TICKET_SOLD_OUT);
        }
        return dailySection;
    }

    private void fillSectionSeat(DailySection dailySection) {
        String key = RedisKeyEnum.DAILY_SECTION_CACHE.getKeyPrefix()
                + dailySection.getDailyTrainId()
                + ":" + dailySection.getStartStop() + "-" + dailySection.getEndStop();
        DailySection cachedDailySection = redisTemplate.opsForValue().get(key);
        if (cachedDailySection != null) {
            BeanUtil.copyProperties(cachedDailySection, dailySection);
            log.info("DailySection Cache HIT " + key + ": {}", cachedDailySection);
            return;
        }

        DailyTrain dailyTrain = dailyTrainMapper.selectByDailyTrainId(dailySection.getDailyTrainId());
        dailySection.setDailyTrainId(dailySection.getDailyTrainId());
        dailySection.setTrainCode(dailyTrain.getCode());

        String pattern = "_".repeat(dailySection.getStartStopIndex())
                + "0".repeat(dailySection.getEndStopIndex() - dailySection.getStartStopIndex()) + "%";
        Integer firstClassSeatCount = dailySeatMapper.countByDailyTrainIdAndTypeAndState(dailyTrain.getDailyTrainId(),
                CarriageTypeEnum.FIRST_CLASS_CARRIAGE.getCode(), pattern);
        Integer secondClassSeatCount = dailySeatMapper.countByDailyTrainIdAndTypeAndState(dailyTrain.getDailyTrainId(),
                CarriageTypeEnum.SECOND_CLASS_CARRIAGE.getCode(), pattern);

        dailySection.setFirstClassSeatCount(firstClassSeatCount);
        dailySection.setSecondClassSeatCount(secondClassSeatCount);

        // TODO 定价策略
        dailySection.setFirstClassSeatPrice(new BigDecimal("500"));
        dailySection.setSecondClassSeatPrice(new BigDecimal("200"));

        redisTemplate.opsForValue().set(key, dailySection);
        log.info("DailySection Cache MISS " + key + ": {}", dailySection);
    }
}
