package com.tuberose.clock.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRes {

    private Long passengerId;

    private String name;

    private String identity;

    private Integer type;

    private Long userId;
}
