package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarriageReq {
    @NotBlank(message = "carriage train code is blank")
    private String trainCode;
    @NotNull(message = "carriage index is null")
    private Integer index;

    @NotNull(message = "carriage type is null")
    private Integer type;
}
