package com.tuberose.clock.business.service;

import com.tuberose.clock.business.request.TrainReq;
import com.tuberose.clock.business.response.TrainRes;
import com.tuberose.clock.common.response.PageRes;

public interface TrainService {
    void save(TrainReq trainReq);

    PageRes<TrainRes> query(TrainReq trainReq, Integer pageNum, Integer pageSize);

    void  delete(Long trainId);
}
