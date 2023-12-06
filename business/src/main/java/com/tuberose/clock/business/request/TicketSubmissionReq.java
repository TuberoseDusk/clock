package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSubmissionReq {
    @NotNull(message = "passenger is null")
    private Long passengerId;

    @NotNull(message = "seat type is null")
    private Integer type;

    private String col;
}
