package com.rw.rra.vms.employment;

import com.rw.rra.vms.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employment {
    @Id
    @GeneratedValue
    private UUID code;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private BigDecimal baseSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus status = EmploymentStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate joiningDate;
}