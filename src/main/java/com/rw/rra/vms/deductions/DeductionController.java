package com.rw.rra.vms.deductions;

import com.rw.rra.vms.deductions.dto.DeductionRequestDTO;
import com.rw.rra.vms.deductions.dto.DeductionResponseDTO;
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
@RequestMapping("/api/v1/deductions")
public class DeductionController {

    private final DeductionService deductionService;

//    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<List<DeductionResponseDTO>> getAllDeductions() {
//        return ResponseEntity.ok(deductionService.getAllDeductions());
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<Page<DeductionResponseDTO>> getAllDeductionsPaged(Pageable pageable) {
        return ResponseEntity.ok(deductionService.getAllDeductions(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<DeductionResponseDTO> getDeductionById(@PathVariable UUID id) {
        return ResponseEntity.ok(deductionService.getDeductionById(id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<DeductionResponseDTO> getDeductionByName(@PathVariable String name) {
        return ResponseEntity.ok(deductionService.getDeductionByName(name));
    }

//    @GetMapping("/search/{name}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<DeductionResponseDTO>> searchDeductionsByName(
//            @PathVariable String name,
//            Pageable pageable) {
//        return ResponseEntity.ok(deductionService.getDeductionsByNameContaining(name, pageable));
//    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<DeductionResponseDTO> createDeduction(
            @Valid
            @RequestBody DeductionRequestDTO deductionDTO,
            UriComponentsBuilder uriBuilder) {
        DeductionResponseDTO createdDeduction = deductionService.createDeduction(deductionDTO);
        var uri = uriBuilder.path("/api/v1/deductions/{id}").buildAndExpand(createdDeduction.getCode()).toUri();
        return ResponseEntity.created(uri).body(createdDeduction);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<DeductionResponseDTO> updateDeduction(
            @PathVariable UUID id,
           @Valid @RequestBody DeductionRequestDTO deductionDTO) {
        return ResponseEntity.ok(deductionService.updateDeduction(id, deductionDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDeduction(@PathVariable UUID id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> initializeDefaultDeductions() {
        deductionService.initializeDefaultDeductions();
        return ResponseEntity.ok().build();
    }
}
