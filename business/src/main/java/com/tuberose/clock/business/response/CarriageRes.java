package com.tuberose.clock.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarriageRes {
    private Long carriageId;

    private String trainCode;
    private Integer index;

    private Integer type;
}
