package com.rw.rra.vms.owners;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    Optional<Owner> findByNationalId(String nationalId);
    Optional<Owner> findByEmail(String email);
    Optional<Owner> findByPhoneNumber(String phoneNumber);
    Optional<Owner> findByNationalIdOrEmailOrPhoneNumber(
            String nationalId,
            String email,
            String phoneNumber
    );
}
