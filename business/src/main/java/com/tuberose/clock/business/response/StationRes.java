package com.tuberose.clock.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationRes {
    private Long stationId;

    private String name;

    private String abbrev;
}
