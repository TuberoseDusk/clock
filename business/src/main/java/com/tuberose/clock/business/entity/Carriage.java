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

    private Integer type;   // 1: 一等座; 2: 二等座； 3: 硬座; 4: 硬卧; 5: 软卧; 6: 无座
    private Integer rowCount;
    private Integer colCount;
}
