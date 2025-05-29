package com.rw.rra.vms.users.DTO;

import com.rw.rra.vms.users.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class RegisterRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be between 2 and 50 characters long")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid.")
    String email;

    @NotBlank(message = "Phone number is required.")
//    @ValidRwandanPhoneNumber
    String phoneNumber;

    @NotBlank(message = "National ID is required.")
//    @ValidRwandaId
    String nationalId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 250, message = "Password must be at least 8 characters long")
    String password;

    @NotNull(message = "Date of birth is required")
    LocalDate dateOfBirth;

    Role role = Role.ROLE_EMPLOYEE; // Default role for employee registration
}
