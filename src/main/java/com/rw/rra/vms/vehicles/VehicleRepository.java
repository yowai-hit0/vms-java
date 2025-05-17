package com.rw.rra.vms.vehicles;

import com.rw.rra.vms.vehicles.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByChassisNumber(String chassis);
}
