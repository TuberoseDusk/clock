package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainReq {

    @NotBlank(message = "train code is blank")
    private String code;
    @NotNull(message = "train type is null")
    private Integer type;

    @NotBlank(message = "train start station is blank")
    private String startStation;
    @NotNull(message = "train start time is null")
    private Time startTime;

    @NotBlank(message = "train end station is blank")
    private String endStation;
    @NotNull(message = "train end time is null")
    private Time endTime;

}
