package com.rw.rra.vms.payroll;

import com.rw.rra.vms.payroll.dto.PayslipResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payroll")
public class PayrollController {

    private final PayrollService payrollService;

//    @GetMapping("/payslips")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<List<PayslipResponseDTO>> getAllPayslips() {
//        return ResponseEntity.ok(payrollService.getAllPayslips());
//    }

    @GetMapping("/payslips/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Page<PayslipResponseDTO>> getAllPayslipsPaged(Pageable pageable) {
        return ResponseEntity.ok(payrollService.getAllPayslips(pageable));
    }

    @GetMapping("/payslips/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<PayslipResponseDTO> getPayslipById(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollService.getPayslipById(id));
    }

    @GetMapping("/payslips/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(payrollService.getPayslipsByEmployee(employeeId));
    }

//    @GetMapping("/payslips/employee/{employeeId}/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<PayslipResponseDTO>> getPayslipsByEmployeePaged(
//            @PathVariable UUID employeeId,
//            Pageable pageable) {
//        return ResponseEntity.ok(payrollService.getPayslipsByEmployee(employeeId, pageable));
//    }

    @GetMapping("/payslips/month/{month}/year/{year}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByMonthAndYear(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.getPayslipsByMonthAndYear(month, year));
    }

//    @GetMapping("/payslips/month/{month}/year/{year}/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<Page<PayslipResponseDTO>> getPayslipsByMonthAndYearPaged(
//            @PathVariable Integer month,
//            @PathVariable Integer year,
//            Pageable pageable) {
//        return ResponseEntity.ok(payrollService.getPayslipsByMonthAndYear(month, year, pageable));
//    }

//    @GetMapping("/payslips/employee/{employeeId}/month/{month}/year/{year}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<PayslipResponseDTO> getPayslipByEmployeeAndMonthAndYear(
//            @PathVariable UUID employeeId,
//            @PathVariable Integer month,
//            @PathVariable Integer year) {
//        return ResponseEntity.ok(payrollService.getPayslipByEmployeeAndMonthAndYear(employeeId, month, year));
//    }

    @PostMapping("/generate/month/{month}/year/{year}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<PayslipResponseDTO>> generatePayrollForMonthAndYear(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.generatePayrollForMonthAndYear(month, year));
    }

    @PatchMapping("/approve/payslip/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PayslipResponseDTO> approvePayslip(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollService.approvePayslip(id));
    }

//    @PatchMapping("/approve/month/{month}/year/{year}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<List<PayslipResponseDTO>> approveAllPayslipsForMonthAndYear(
//            @PathVariable Integer month,
//            @PathVariable Integer year) {
//        return ResponseEntity.ok(payrollService.approveAllPayslipsForMonthAndYear(month, year));
//    }
}
