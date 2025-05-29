package com.rw.rra.vms.deductions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, UUID> {
    Optional<Deduction> findByDeductionName(String deductionName);
    boolean existsByDeductionName(String deductionName);

    Page<Deduction> findAll(Pageable pageable);
    Page<Deduction> findByDeductionNameContaining(String deductionName, Pageable pageable);
}
