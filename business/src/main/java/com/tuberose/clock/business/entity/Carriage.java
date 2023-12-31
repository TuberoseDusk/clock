package com.tuberose.clock.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carriage {

    private Long carriageId;

    private String trainCode;
    private Integer index;

    private Integer type;
}
