package com.tuberose.clock.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerReq {

    private Long passengerId;

    @NotBlank(message = "identity is blank")
    private String identity;

    @NotNull(message = "type is blank")
    private Integer type;

}
