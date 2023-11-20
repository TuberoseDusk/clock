package com.tuberose.clock.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReq {
    @NotBlank(message = "username is blank")
    private String username;
    @NotBlank(message = "password is blank")
    private String password;
}
