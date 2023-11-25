package com.tuberose.clock.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tuberose.clock.business.entity.Carriage;
import com.tuberose.clock.business.entity.Train;
import com.tuberose.clock.business.mapper.CarriageMapper;
import com.tuberose.clock.business.mapper.TrainMapper;
import com.tuberose.clock.business.request.CarriageReq;
import com.tuberose.clock.business.service.CarriageService;
import com.tuberose.clock.common.enums.ErrorCodeEnum;
import com.tuberose.clock.common.exception.BusinessException;
import com.tuberose.clock.common.util.Snowflake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CarriageServiceImpl implements CarriageService {

    @Resource
    private CarriageMapper carriageMapper;

    @Resource
    private TrainMapper trainMapper;

    @Override
    public void save(CarriageReq carriageReq) {
        Train train = trainMapper.selectByCode(carriageReq.getTrainCode());
        if (train == null) {
            throw new BusinessException(ErrorCodeEnum.TRAIN_CODE_NOT_EXIST);
        }

        List<Carriage> carriages = carriageMapper.selectByTrainCodeOrderByIndex(carriageReq.getTrainCode());
        if(carriages == null || carriages.isEmpty()) {
            if (carriageReq.getIndex() != 1) {
                throw new BusinessException(ErrorCodeEnum.ILLEGAL_CARRIAGE_INDEX);
            }
        } else {
            Carriage firstCarriage = carriages.get(0);
            Carriage lastCarriage = carriages.get(carriages.size() - 1);
            if (carriageReq.getIndex() < firstCarriage.getIndex() ||
                    carriageReq.getIndex() > lastCarriage.getIndex() + 1) {
                throw new BusinessException(ErrorCodeEnum.ILLEGAL_CARRIAGE_INDEX);
            }

            Collections.reverse(carriages);
            for (Carriage existCarriage : carriages) {
                if (existCarriage.getIndex() >= carriageReq.getIndex()) {
                    carriageMapper.updateIndex(existCarriage.getCarriageId(), existCarriage.getIndex() + 1);
                }
            }
        }

        Carriage carriage = BeanUtil.copyProperties(carriageReq, Carriage.class);
        carriage.setCarriageId(Snowflake.nextId());

        carriageMapper.insert(carriage);
    }
}
