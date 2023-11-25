package com.tuberose.clock.business.service;

import com.tuberose.clock.business.request.StopReq;
import com.tuberose.clock.business.response.StopRes;

import java.util.List;

public interface StopService {
    void save(StopReq stopReq);

    List<StopRes> queryByTrainCode(String trainCode);

    void delete(Long stopId);
}
