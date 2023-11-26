package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private Long seatId;

    private String trainCode;
    private Integer carriageIndex;
    private Integer type;

    private String row;
    private String col;
    private Integer number;
}
