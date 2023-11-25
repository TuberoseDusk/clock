package com.tuberose.clock.business.controller.admin;

import com.tuberose.clock.business.request.StopReq;
import com.tuberose.clock.business.response.StopRes;
import com.tuberose.clock.business.service.StopService;
import com.tuberose.clock.common.response.BaseRes;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/stop")
public class StopAdminController {

    @Resource
    private StopService stopService;

    @PostMapping("/save")
    public BaseRes<Void> save(@Valid @RequestBody StopReq stopReq) {
        stopService.save(stopReq);
        return BaseRes.success();
    }

    @GetMapping("/query/{trainCode}")
    public BaseRes<List<StopRes>> query(@PathVariable String trainCode) {
        List<StopRes> stopResList = stopService.queryByTrainCode(trainCode);
        return BaseRes.success(stopResList);
    }

    @DeleteMapping("/delete/{stopId}")
    public BaseRes<Void> delete(@PathVariable Long stopId) {
        stopService.delete(stopId);
        return BaseRes.success();
    }
}
