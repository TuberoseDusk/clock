package com.tuberose.clock.business.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationReq {
    @NotBlank(message = "station name is blank")
    private String name;

    @NotBlank(message = "station abbrev is blank")
    private String abbrev;
}
