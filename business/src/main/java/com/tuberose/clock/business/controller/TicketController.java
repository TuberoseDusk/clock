package com.tuberose.clock.business.controller;

import com.tuberose.clock.business.entity.DailySection;
import com.tuberose.clock.business.service.SectionService;
import com.tuberose.clock.common.response.BaseRes;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Resource
    private SectionService sectionService;

    @GetMapping("/query/{date}/{startStop}/{endStop}")
    public BaseRes<List<DailySection>> query(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                             @PathVariable String startStop, @PathVariable String endStop) {
        List<DailySection> dailySections = sectionService.query(date, startStop, endStop);
        return BaseRes.success(dailySections);
    }
}
