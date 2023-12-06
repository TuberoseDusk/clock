package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmissionReq {
    @NotNull(message = "daily train id is is null")
    private Long dailyTrainId;

    @NotBlank(message = "start stop is blank")
    private String startStop;

    @NotBlank(message = "end stop is blank")
    private String endStop;

    List<TicketSubmissionReq> ticketSubmissionReqs;
}
