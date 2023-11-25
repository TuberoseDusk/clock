package com.tuberose.clock.business.controller.admin;

import com.tuberose.clock.business.request.CarriageReq;
import com.tuberose.clock.business.service.CarriageService;
import com.tuberose.clock.common.response.BaseRes;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/carriage")
public class CarriageAdminController {
    @Resource
    private CarriageService carriageService;

    @PostMapping("/save")
    public BaseRes<Void> save(@Valid @RequestBody CarriageReq carriageReq) {
        carriageService.save(carriageReq);
        return BaseRes.success();
    }
}
