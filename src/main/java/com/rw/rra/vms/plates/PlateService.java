package com.rw.rra.vms.plates;

import com.rw.rra.vms.plates.DTO.PlateRequestDTO;
import com.rw.rra.vms.plates.DTO.PlateResponseDTO;
import com.rw.rra.vms.utils.PlateGeneratorUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlateService {

    private final PlateRepository repo;
    private final PlateMapper mapper;
    private final PlateGeneratorUtility plateUtil;

    public PlateService(PlateRepository repo,
                        PlateMapper mapper,
                        PlateGeneratorUtility plateUtil) {
        this.repo = repo;
        this.mapper = mapper;
        this.plateUtil = plateUtil;
    }

    @Transactional
    public PlateResponseDTO issuePlate(PlateRequestDTO dto) {
        log.info("Issuing plate for owner {}", dto.getOwnerId());
        PlateNumber plate = mapper.toEntity(dto);

        if (dto.getPlateNumber() == null || dto.getPlateNumber().isBlank()) {
            plate.setPlateNumber(plateUtil.generateUniquePlateNumber());
        }

        PlateNumber saved = repo.save(plate);
//        log.debug("Issued plate id={}", saved.getId());
        return mapper.toDTO(saved);
    }

    public List<PlateResponseDTO> listByOwner(UUID ownerId) {
        log.info("Listing plates for owner {}", ownerId);
        return repo.findAllByOwnerId(ownerId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public PlateResponseDTO getById(UUID id) {
        log.info("Fetching plate {}", id);
        PlateNumber p = repo.findById(id).orElseThrow(() -> PlateNotFoundException.byId(id.toString()));
        return mapper.toDTO(p);
    }
}
