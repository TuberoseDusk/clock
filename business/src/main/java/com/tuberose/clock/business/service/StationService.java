package com.tuberose.clock.business.service;

import com.tuberose.clock.business.request.StationReq;
import com.tuberose.clock.business.response.StationRes;
import com.tuberose.clock.common.response.PageRes;

public interface StationService {
    void save(StationReq stationReq);

    void delete(Long stationId);

    PageRes<StationRes> query(StationReq stationReq, Integer pageNum, Integer pageSize);
}
