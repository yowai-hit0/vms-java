package com.rw.rra.vms.employment;

import com.rw.rra.vms.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
    List<Employment> findByEmployee(User employee);
    Page<Employment> findByEmployee(User employee, Pageable pageable);

    List<Employment> findByStatus(EmploymentStatus status);
    Page<Employment> findByStatus(EmploymentStatus status, Pageable pageable);

    Optional<Employment> findByEmployeeAndStatus(User employee, EmploymentStatus status);

    Page<Employment> findAll(Pageable pageable);
}
