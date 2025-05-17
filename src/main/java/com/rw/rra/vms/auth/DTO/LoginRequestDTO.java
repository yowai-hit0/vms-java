package com.rw.rra.vms.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "Field is required")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
