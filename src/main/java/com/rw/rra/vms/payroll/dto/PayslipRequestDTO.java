package com.rw.rra.vms.payroll.dto;

import com.rw.rra.vms.payroll.PayslipStatus;
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
public class PayslipRequestDTO {
    private UUID employeeId;
    private Integer month;
    private Integer year;
    private PayslipStatus status;
}