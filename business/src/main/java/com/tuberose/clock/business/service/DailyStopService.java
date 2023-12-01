package com.tuberose.clock.business.service;

import java.time.LocalDate;

public interface DailyStopService {
    void generate(LocalDate date, String trainCode);
}
