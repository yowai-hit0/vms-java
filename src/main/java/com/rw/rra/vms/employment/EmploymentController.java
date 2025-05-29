package com.rw.rra.vms.employment;

import com.rw.rra.vms.employment.dto.EmploymentRequestDTO;
import com.rw.rra.vms.employment.dto.EmploymentResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employments")
public class EmploymentController {

    private final EmploymentService employmentService;

//    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<List<EmploymentResponseDTO>> getAllEmployments() {
//        return ResponseEntity.ok(employmentService.getAllEmployments());
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Page<EmploymentResponseDTO>> getAllEmploymentsPaged(Pageable pageable) {
        return ResponseEntity.ok(employmentService.getAllEmployments(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<EmploymentResponseDTO> getEmploymentById(@PathVariable UUID id) {
        return ResponseEntity.ok(employmentService.getEmploymentById(id));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmploymentResponseDTO>> getEmploymentsByEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(employmentService.getEmploymentsByEmployee(employeeId));
    }

//    @GetMapping("/employee/{employeeId}/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<EmploymentResponseDTO>> getEmploymentsByEmployeePaged(
//            @PathVariable UUID employeeId,
//            Pageable pageable) {
//        return ResponseEntity.ok(employmentService.getEmploymentsByEmployee(employeeId, pageable));
//    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<EmploymentResponseDTO>> getActiveEmployments() {
        return ResponseEntity.ok(employmentService.getActiveEmployments());
    }

//    @GetMapping("/active/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<Page<EmploymentResponseDTO>> getActiveEmploymentsPaged(Pageable pageable) {
//        return ResponseEntity.ok(employmentService.getActiveEmployments(pageable));
//    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<EmploymentResponseDTO> createEmployment(
            @Valid @RequestBody EmploymentRequestDTO employmentDTO,
            UriComponentsBuilder uriBuilder) {
        EmploymentResponseDTO createdEmployment = employmentService.createEmployment(employmentDTO);
        var uri = uriBuilder.path("/api/v1/employments/{id}").buildAndExpand(createdEmployment.getCode()).toUri();
        return ResponseEntity.created(uri).body(createdEmployment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<EmploymentResponseDTO> updateEmployment(
            @PathVariable UUID id,
           @Valid @RequestBody EmploymentRequestDTO employmentDTO) {
        return ResponseEntity.ok(employmentService.updateEmployment(id, employmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEmployment(@PathVariable UUID id) {
        employmentService.deleteEmployment(id);
        return ResponseEntity.noContent().build();
    }
}
