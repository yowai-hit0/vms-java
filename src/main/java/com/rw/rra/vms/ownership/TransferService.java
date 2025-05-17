package com.rw.rra.vms.ownership;

import com.rw.rra.vms.owners.OwnerRepository;
import com.rw.rra.vms.ownership.DTO.TransferRequestDTO;
import com.rw.rra.vms.ownership.DTO.TransferResponseDTO;
import com.rw.rra.vms.plates.PlateRepository;
import com.rw.rra.vms.plates.PlateStatus;
import com.rw.rra.vms.vehicles.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferService {

    private final TransferRepository transferRepo;
    private final TransferMapper mapper;
    private final VehicleRepository vehicleRepo;
    private final OwnerRepository ownerRepo;
    private final PlateRepository plateRepo;

    public TransferService(TransferRepository transferRepo,
                           TransferMapper mapper,
                           VehicleRepository vehicleRepo,
                           OwnerRepository ownerRepo,
                           PlateRepository plateRepo) {
        this.transferRepo = transferRepo;
        this.mapper = mapper;
        this.vehicleRepo = vehicleRepo;
        this.ownerRepo = ownerRepo;
        this.plateRepo = plateRepo;
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO dto) {
        log.info("Transferring vehicle {} to owner {} with new plate {}",
                dto.getVehicleId(), dto.getNewOwnerId(), dto.getNewPlateId());

        // 1) load existing entities
        var vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> TransferException.vehicleNotFound(dto.getVehicleId().toString()));
        var fromOwner = vehicle.getOwner();
        var oldPlate  = vehicle.getPlateNumber();

        var toOwner = ownerRepo.findById(dto.getNewOwnerId())
                .orElseThrow(() -> TransferException.ownerNotFound(dto.getNewOwnerId().toString()));
        var newPlate = plateRepo.findById(dto.getNewPlateId())
                .orElseThrow(() -> TransferException.plateNotFound(dto.getNewPlateId().toString()));

        if (newPlate.getStatus() != PlateStatus.AVAILABLE) {
            throw TransferException.invalidPlate(newPlate.getId().toString());
        }

        // 2) update statuses
        oldPlate.setStatus(PlateStatus.AVAILABLE);
        newPlate.setStatus(PlateStatus.IN_USE);

        // 3) update vehicle
        vehicle.setOwner(toOwner);
        vehicle.setPlateNumber(newPlate);

        // 4) build transfer record
        OwnershipTransfer transfer = mapper.toEntity(dto);
        transfer.setVehicle(vehicle);
        transfer.setFromOwner(fromOwner);
        transfer.setToOwner(toOwner);
        transfer.setOldPlate(oldPlate);
        transfer.setNewPlate(newPlate);

        var saved = transferRepo.save(transfer);
        log.debug("Created transfer id={}", saved.getId());

        // 5) return DTO
        return mapper.toDTO(saved);
    }

    public List<TransferResponseDTO> historyByVehicle(UUID vehicleId) {
        log.info("Loading transfer history for vehicle {}", vehicleId);
        return transferRepo.findAllByVehicleIdOrderByTransferDateDesc(vehicleId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<TransferResponseDTO> listAllForReport() {
        return transferRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

}