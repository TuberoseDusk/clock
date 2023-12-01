package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopReq {
    @NotBlank(message = "stop train code is blank")
    private String trainCode;

    @NotNull(message = "stop index is null")
    private Integer index;

    @NotBlank(message = "stop name is blank")
    private String name;

    @NotNull(message = "stop arrival time is null")
    private Integer arrivalTime;

    @NotNull(message = "stop departure time is null")
    private Integer departureTime;
}
