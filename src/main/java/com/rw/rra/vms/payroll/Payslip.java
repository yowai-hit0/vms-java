package com.rw.rra.vms.payroll;

import com.rw.rra.vms.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payslips", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "month", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payslip {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private BigDecimal houseAmount;

    @Column(nullable = false)
    private BigDecimal transportAmount;

    @Column(nullable = false)
    private BigDecimal employeeTaxedAmount;

    @Column(nullable = false)
    private BigDecimal pensionAmount;

    @Column(nullable = false)
    private BigDecimal medicalInsuranceAmount;

    @Column(nullable = false)
    private BigDecimal otherTaxedAmount;

    @Column(nullable = false)
    private BigDecimal grossSalary;

    @Column(nullable = false)
    private BigDecimal netSalary;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipStatus status = PayslipStatus.PENDING;
}