package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSubmissionReq {
    @NotNull(message = "passenger is null")
    private Long passengerId;

    @NotBlank(message = "train code is blank")
    private String trainCode;

    @NotNull(message = "seat type is null")
    private Integer type;

    private String col;

    @NotBlank(message = "start stop is blank")
    private String startStop;

    @NotNull(message = "start time is blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotBlank(message = "end stop is blank")
    private String endStop;
}
