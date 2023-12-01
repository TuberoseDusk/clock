package com.tuberose.clock.business.service;

import java.time.LocalDate;

public interface DailyCarriageService {
    void generate(LocalDate date, String trainCode);
}
