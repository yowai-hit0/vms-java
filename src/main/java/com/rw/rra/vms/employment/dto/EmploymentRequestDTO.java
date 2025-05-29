package com.rw.rra.vms.employment.dto;

import com.rw.rra.vms.employment.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class EmploymentRequestDTO {
    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department name must be less than 100 characters")
    private String department;

    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position name must be less than 100 characters")
    private String position;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus status;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;
}
