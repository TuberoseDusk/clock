package com.tuberose.clock.business.service;

import com.tuberose.clock.business.request.OrderSubmissionReq;

public interface SellerService {
    void confirmOrder(OrderSubmissionReq orderSubmissionReq);
}
