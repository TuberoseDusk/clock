package com.tuberose.clock.business.service;

import com.tuberose.clock.business.entity.DailySection;

import java.time.LocalDate;
import java.util.List;

public interface SectionService {
    List<DailySection> query(LocalDate date, String startStop, String endStop);
}
