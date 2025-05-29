package com.rw.rra.vms.deductions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeductionResponseDTO {
    private UUID code;
    private String deductionName;
    private BigDecimal percentage;
}