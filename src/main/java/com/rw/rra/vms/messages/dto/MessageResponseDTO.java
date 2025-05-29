package com.rw.rra.vms.messages.dto;

import com.rw.rra.vms.users.DTO.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {
    private UUID id;
    private UserResponseDTO employee;
    private String message;
    private Integer month;
    private Integer year;
    private boolean sent;
    private LocalDateTime sentAt;
}