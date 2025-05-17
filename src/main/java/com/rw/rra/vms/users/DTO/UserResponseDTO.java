package com.rw.rra.vms.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class UserResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
