package com.tuberose.clock.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    private Long passengerId;

    private String identity;

    private Long userId;

    private Integer type;
}
