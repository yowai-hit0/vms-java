package com.rw.rra.vms.deductions;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deduction {
    @Id
    @GeneratedValue
    private UUID code;

    @Column(nullable = false, unique = true)
    private String deductionName;

    @Column(nullable = false)
    private BigDecimal percentage;
}