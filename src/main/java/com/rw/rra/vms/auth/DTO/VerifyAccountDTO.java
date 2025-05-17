package com.rw.rra.vms.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class VerifyAccountDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid.")
    String email;

    @Size(min = 6, max = 6, message = "OTP must be 6 digits long.")
    String otp;

}
