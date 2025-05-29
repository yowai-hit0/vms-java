package com.rw.rra.vms.users.DTO;

import com.rw.rra.vms.users.Role;
import com.rw.rra.vms.users.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class UserResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private Role role;
    private LocalDate dateOfBirth;
    private UserStatus status;
    private boolean enabled;
}
