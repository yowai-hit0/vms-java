package com.rw.rra.vms.plates;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlateRepository extends JpaRepository<PlateNumber, UUID> {
    Optional<PlateNumber> findByPlateNumber(String plateNumber);
    List<PlateNumber> findAllByOwnerId(UUID ownerId);
}
