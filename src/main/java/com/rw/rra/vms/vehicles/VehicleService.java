package com.rw.rra.vms.vehicles;

import com.rw.rra.vms.owners.OwnerRepository;
import com.rw.rra.vms.plates.PlateRepository;
import com.rw.rra.vms.plates.PlateStatus;
import com.rw.rra.vms.vehicles.DTO.VehicleRequestDTO;
import com.rw.rra.vms.vehicles.DTO.VehicleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleService {

    private final VehicleRepository repo;
    private final OwnerRepository ownerRepo;
    private final PlateRepository plateRepo;
    private final VehicleMapper mapper;

    public VehicleService(VehicleRepository repo,
                          OwnerRepository ownerRepo,
                          PlateRepository plateRepo,
                          VehicleMapper mapper) {
        this.repo = repo;
        this.ownerRepo = ownerRepo;
        this.plateRepo = plateRepo;
        this.mapper = mapper;
    }

    @Transactional
    public VehicleResponseDTO register(VehicleRequestDTO dto) {
        log.info("Registering vehicle {}", dto.getChassisNumber());
        var vehicle = mapper.toEntity(dto);

        var owner = ownerRepo.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        var plate  = plateRepo.findById(dto.getPlateId())
                .orElseThrow(() -> new RuntimeException("Plate not found"));

        vehicle.setOwner(owner);
        vehicle.setPlateNumber(plate);
        plate.setStatus(PlateStatus.IN_USE);

        var saved = repo.save(vehicle);
        log.debug("Registered vehicle id={}", saved.getId());
        return mapper.toDTO(saved);
    }

    public Page<VehicleResponseDTO> listAll(Pageable pg) {
        log.info("Listing vehicles page={} size={}", pg.getPageNumber(), pg.getPageSize());
        return repo.findAll(pg).map(mapper::toDTO);
    }

    public VehicleResponseDTO getById(UUID id) {
        log.info("Fetching vehicle {}", id);
        var v = repo.findById(id).orElseThrow(() -> VehicleNotFoundException.byId(id.toString()));
        return mapper.toDTO(v);
    }

    public VehicleResponseDTO getByChassis(String ch) {
        log.info("Searching by chassis {}", ch);
        var v = repo.findByChassisNumber(ch).orElseThrow(() -> VehicleNotFoundException.byChassis(ch));
        return mapper.toDTO(v);
    }

    public List<VehicleResponseDTO> listAllForReport() {
        // load all
        return repo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
