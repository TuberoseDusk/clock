package com.tuberose.clock.business.controller.admin;

import com.tuberose.clock.business.service.DailyTrainService;
import com.tuberose.clock.common.response.BaseRes;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/daily/train")
public class DailyTrainAdminController {

    @Resource
    DailyTrainService dailyTrainService;

    @PostMapping("/generate/{date}")
    public BaseRes<Void> generate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        dailyTrainService.generateAll(date);
        return BaseRes.success();
    }
}
