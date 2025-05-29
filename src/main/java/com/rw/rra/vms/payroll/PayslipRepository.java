package com.rw.rra.vms.payroll;

import com.rw.rra.vms.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, UUID> {
    List<Payslip> findByEmployee(User employee);
    Page<Payslip> findByEmployee(User employee, Pageable pageable);

    List<Payslip> findByStatus(PayslipStatus status);
    Page<Payslip> findByStatus(PayslipStatus status, Pageable pageable);

    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    Page<Payslip> findByMonthAndYear(Integer month, Integer year, Pageable pageable);

    List<Payslip> findByEmployeeAndStatus(User employee, PayslipStatus status);
    Page<Payslip> findByEmployeeAndStatus(User employee, PayslipStatus status, Pageable pageable);

    Optional<Payslip> findByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);
    boolean existsByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);

    Page<Payslip> findAll(Pageable pageable);
}
