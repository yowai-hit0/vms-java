package com.rw.rra.vms.deductions.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeductionRequestDTO {
    @NotBlank(message = "Deduction name is required")
    @Size(max = 100, message = "Deduction name must be less than 100 characters")
    private String deductionName;

    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Percentage must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage must be at most 100")
    private BigDecimal percentage;
}
