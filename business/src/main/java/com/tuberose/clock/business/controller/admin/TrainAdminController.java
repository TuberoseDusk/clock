package com.tuberose.clock.business.controller.admin;

import com.tuberose.clock.business.request.TrainReq;
import com.tuberose.clock.business.response.TrainRes;
import com.tuberose.clock.business.service.TrainService;
import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.common.response.PageRes;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {
    @Resource
    TrainService trainService;

    @PostMapping("/save")
    public BaseRes<Void> save(@Valid @RequestBody TrainReq trainReq) {
        trainService.save(trainReq);
        return BaseRes.success();
    }

    @GetMapping("/query/{pageNum}/{pageSize}")
    public PageRes<TrainRes> query(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                   @RequestBody(required = false) TrainReq trainReq) {
        return trainService.query(trainReq, pageNum, pageSize);
    }
}
