package com.rw.rra.vms.payroll.dto;

import com.rw.rra.vms.payroll.PayslipStatus;
import com.rw.rra.vms.users.DTO.UserResponseDTO;
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
public class PayslipResponseDTO {
    private UUID id;
    private UserResponseDTO employee;
    private BigDecimal houseAmount;
    private BigDecimal transportAmount;
    private BigDecimal employeeTaxedAmount;
    private BigDecimal pensionAmount;
    private BigDecimal medicalInsuranceAmount;
    private BigDecimal otherTaxedAmount;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private Integer month;
    private Integer year;
    private PayslipStatus status;
}