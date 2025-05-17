package com.rw.rra.vms.ownership;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<OwnershipTransfer, UUID> {
    List<OwnershipTransfer> findAllByVehicleIdOrderByTransferDateDesc(UUID vehicleId);
}
