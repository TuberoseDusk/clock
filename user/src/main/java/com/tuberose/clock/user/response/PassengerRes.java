package com.tuberose.clock.user.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRes {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long passengerId;

    private String identity;

    private Integer type;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
}
