package com.rw.rra.vms.employment.dto;

import com.rw.rra.vms.employment.EmploymentStatus;
import com.rw.rra.vms.users.DTO.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentResponseDTO {
    private UUID code;
    private UserResponseDTO employee;
    private String department;
    private String position;
    private BigDecimal baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;
}