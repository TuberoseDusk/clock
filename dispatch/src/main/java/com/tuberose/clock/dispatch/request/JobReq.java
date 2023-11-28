package com.tuberose.clock.dispatch.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobReq {
    private String group;
    private String name;
    private String description;
    private String cronExpression;
}
