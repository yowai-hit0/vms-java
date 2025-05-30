package com.rw.rra.vms.payroll;

import com.rw.rra.vms.common.exceptions.ResourceNotFoundException;
import com.rw.rra.vms.deductions.Deduction;
import com.rw.rra.vms.deductions.DeductionRepository;
import com.rw.rra.vms.email.EmailService;
import com.rw.rra.vms.employment.Employment;
import com.rw.rra.vms.employment.EmploymentRepository;
import com.rw.rra.vms.employment.EmploymentStatus;
import com.rw.rra.vms.messages.Message;
import com.rw.rra.vms.messages.MessageRepository;
import com.rw.rra.vms.payroll.dto.PayslipMapper;
import com.rw.rra.vms.payroll.dto.PayslipRequestDTO;
import com.rw.rra.vms.payroll.dto.PayslipResponseDTO;
import com.rw.rra.vms.users.User;
import com.rw.rra.vms.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayslipRepository payslipRepository;
    private final UserRepository userRepository;
    private final EmploymentRepository employmentRepository;
    private final DeductionRepository deductionRepository;
    private final MessageRepository messageRepository;
    private final PayslipMapper payslipMapper;
    private final EmailService emailService;

//    @Transactional(readOnly = true)
//    public List<PayslipResponseDTO> getAllPayslips() {
//        return payslipMapper.toResponseDTOList(payslipRepository.findAll());
//    }

    @Transactional(readOnly = true)
    public Page<PayslipResponseDTO> getAllPayslips(Pageable pageable) {
        return payslipRepository.findAll(pageable)
                .map(payslipMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PayslipResponseDTO getPayslipById(UUID id) {
        return payslipRepository.findById(id)
                .map(payslipMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PayslipResponseDTO> getPayslipsByEmployee(UUID employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return payslipMapper.toResponseDTOList(payslipRepository.findByEmployee(employee));
    }

//    @Transactional(readOnly = true)
//    public Page<PayslipResponseDTO> getPayslipsByEmployee(UUID employeeId, Pageable pageable) {
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
//        return payslipRepository.findByEmployee(employee, pageable)
//                .map(payslipMapper::toResponseDTO);
//    }

    @Transactional(readOnly = true)
    public List<PayslipResponseDTO> getPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipMapper.toResponseDTOList(payslipRepository.findByMonthAndYear(month, year));
    }

//    @Transactional(readOnly = true)
//    public Page<PayslipResponseDTO> getPayslipsByMonthAndYear(Integer month, Integer year, Pageable pageable) {
//        return payslipRepository.findByMonthAndYear(month, year, pageable)
//                .map(payslipMapper::toResponseDTO);
//    }

    @Transactional(readOnly = true)
    public PayslipResponseDTO getPayslipByEmployeeAndMonthAndYear(UUID employeeId, Integer month, Integer year) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        return payslipRepository.findByEmployeeAndMonthAndYear(employee, month, year)
                .map(payslipMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payslip not found for employee id: " + employeeId + " for month: " + month + " and year: " + year));
    }

    @Transactional
    public List<PayslipResponseDTO> generatePayrollForMonthAndYear(Integer month, Integer year) {
        // Get all active employments
        List<Employment> activeEmployments = employmentRepository.findByStatus(EmploymentStatus.ACTIVE);

        // Get all deductions
        List<Deduction> deductions = deductionRepository.findAll();
        Map<String, BigDecimal> deductionMap = new HashMap<>();
        for (Deduction deduction : deductions) {
            deductionMap.put(deduction.getDeductionName(), deduction.getPercentage());
        }

        // Generate payslips for each active employment
        for (Employment employment : activeEmployments) {
            User employee = employment.getEmployee();

            // Check if payslip already exists for this employee for the given month and year
            if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                continue; // Skip if payslip already exists
            }

            // Calculate payslip amounts
            BigDecimal baseSalary = employment.getBaseSalary();

            // Housing and Transport are added to base salary to get gross salary
            BigDecimal housingPercentage = deductionMap.getOrDefault("Housing", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal transportPercentage = deductionMap.getOrDefault("Transport", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal housingAmount = baseSalary.multiply(housingPercentage).setScale(2, RoundingMode.HALF_UP);
            BigDecimal transportAmount = baseSalary.multiply(transportPercentage).setScale(2, RoundingMode.HALF_UP);

            BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount).setScale(2, RoundingMode.HALF_UP);

            // Calculate deductions
            BigDecimal employeeTaxPercentage = deductionMap.getOrDefault("Employee Tax", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal pensionPercentage = deductionMap.getOrDefault("Pension", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal medicalInsurancePercentage = deductionMap.getOrDefault("Medical Insurance", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal otherPercentage = deductionMap.getOrDefault("Others", BigDecimal.ZERO).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            BigDecimal employeeTaxAmount = baseSalary.multiply(employeeTaxPercentage).setScale(2, RoundingMode.HALF_UP);
            BigDecimal pensionAmount = baseSalary.multiply(pensionPercentage).setScale(2, RoundingMode.HALF_UP);
            BigDecimal medicalInsuranceAmount = baseSalary.multiply(medicalInsurancePercentage).setScale(2, RoundingMode.HALF_UP);
            BigDecimal otherAmount = baseSalary.multiply(otherPercentage).setScale(2, RoundingMode.HALF_UP);

            // Calculate net salary
            BigDecimal totalDeductions = employeeTaxAmount.add(pensionAmount).add(medicalInsuranceAmount).add(otherAmount);
            BigDecimal netSalary = grossSalary.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);

            // Create and save payslip
            Payslip payslip = new Payslip();
            payslip.setEmployee(employee);
            payslip.setHouseAmount(housingAmount);
            payslip.setTransportAmount(transportAmount);
            payslip.setEmployeeTaxedAmount(employeeTaxAmount);
            payslip.setPensionAmount(pensionAmount);
            payslip.setMedicalInsuranceAmount(medicalInsuranceAmount);
            payslip.setOtherTaxedAmount(otherAmount);
            payslip.setGrossSalary(grossSalary);
            payslip.setNetSalary(netSalary);
            payslip.setMonth(month);
            payslip.setYear(year);
            payslip.setStatus(PayslipStatus.PENDING);

            payslipRepository.save(payslip);
        }

        return payslipMapper.toResponseDTOList(payslipRepository.findByMonthAndYear(month, year));
    }

    @Transactional
    public PayslipResponseDTO approvePayslip(UUID id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found with id: " + id));

        payslip.setStatus(PayslipStatus.PAID);
        Payslip savedPayslip = payslipRepository.save(payslip);

        // Create message for notification
        Message message = new Message();
        message.setEmployee(payslip.getEmployee());
        message.setMonth(payslip.getMonth());
        message.setYear(payslip.getYear());

        String messageText = String.format(
                "Dear %s, your salary for %d/%d from RCA amounting to %s has been credited to your account %s successfully.",
                payslip.getEmployee().getFirstName(),
                payslip.getMonth(),
                payslip.getYear(),
                payslip.getNetSalary().toString(),
                payslip.getEmployee().getId().toString()
        );

        message.setMessage(messageText);
        message.setSent(true);
        messageRepository.save(message);
        emailService.sendSalaryNotification(payslip.getEmployee().getEmail(), payslip.getEmployee().getFirstName(), messageText);


        return payslipMapper.toResponseDTO(savedPayslip);
    }

//    @Transactional
//    public List<PayslipResponseDTO> approveAllPayslipsForMonthAndYear(Integer month, Integer year) {
//        // Validate inputs
//        if (month == null || month < 1 || month > 12) {
//            throw new IllegalArgumentException("Month must be between 1 and 12");
//        }
//        if (year == null || year < 1900 || year > 9999) {
//            throw new IllegalArgumentException("Year must be a valid four-digit number");
//        }
//
//        // Fetch pending payslips (ensure Employee is eagerly fetched if needed)
//        List<Payslip> pendingPayslips = payslipRepository.findByMonthAndYear(month, year)
//                .stream()
//                .filter(p -> p.getStatus() == PayslipStatus.PENDING)
//                .toList();
//
//        List<Message> messagesToSave = new ArrayList<>();
//        List<Payslip> payslipsToSave = new ArrayList<>();
//
//        for (Payslip payslip : pendingPayslips) {
//            // Update payslip status
//            payslip.setStatus(PayslipStatus.PAID);
//            payslipsToSave.add(payslip);
//
//            // Create message for notification
//            try {
//                User employee = payslip.getEmployee();
//                if (employee == null || employee.getFirstName() == null || employee.getEmail() == null || payslip.getNetSalary() == null) {
//                    log.error("Invalid data for payslip ID: {}", payslip.getId());
//                    continue; // Skip invalid payslips
//                }
//
//                String messageText = String.format(
//                        "Dear %s, your salary for %d/%d from RCA amounting to %s has been credited to your account %s successfully.",
//                        employee.getFirstName(),
//                        payslip.getMonth(),
//                        payslip.getYear(),
//                        payslip.getNetSalary().toString(),
//                        employee.getId().toString()
//                );
//
//                Message message = new Message();
//                message.setEmployee(employee);
//                message.setMonth(payslip.getMonth());
//                message.setYear(payslip.getYear());
//                message.setMessage(messageText);
//                message.setSent(true);
//                messagesToSave.add(message);
//
//                // Send email outside transaction to avoid rollback
//                try {
//                    emailService.sendSalaryNotification(employee.getEmail(), employee.getFirstName(), messageText);
//                } catch (Exception e) {
//                    log.error("Failed to send email for employee ID {}: {}", employee.getId(), e.getMessage());
//                }
//            } catch (Exception e) {
//                log.error("Error processing payslip ID {}: {}", payslip.getId(), e.getMessage());
//            }
//        }
//
//        // Batch save payslips and messages
//        payslipRepository.saveAll(payslipsToSave);
//        messageRepository.saveAll(messagesToSave);
//
//        // Return only updated payslips
//        return payslipMapper.toResponseDTOList(payslipsToSave);
//    }
}