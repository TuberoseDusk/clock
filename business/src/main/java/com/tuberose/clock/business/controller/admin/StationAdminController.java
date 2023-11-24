package com.tuberose.clock.business.controller.admin;

import com.tuberose.clock.business.request.StationReq;
import com.tuberose.clock.business.response.StationRes;
import com.tuberose.clock.business.service.StationService;
import com.tuberose.clock.common.response.BaseRes;
import com.tuberose.clock.common.response.PageRes;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    @Resource
    StationService stationService;

    @PostMapping("/save")
    public BaseRes<Void> save(@Valid @RequestBody StationReq stationReq) {
        stationService.save(stationReq);
        return BaseRes.success();
    }

    @DeleteMapping("/delete/{stationId}")
    public BaseRes<Void> delete(@PathVariable Long stationId) {
        stationService.delete(stationId);
        return BaseRes.success();
    }

    @GetMapping("/query/{pageNum}/{pageSize}")
    public PageRes<StationRes> query(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody(required = false) StationReq stationReq) {
        return stationService.query(stationReq, pageNum, pageSize);
    }
}
