package com.rw.rra.vms.plates;

import com.rw.rra.vms.plates.DTO.PlateRequestDTO;
import com.rw.rra.vms.plates.DTO.PlateResponseDTO;
import com.rw.rra.vms.plates.PlateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plates")
@Slf4j
@Validated
public class PlateController {

    private final PlateService service;

    public PlateController(PlateService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PlateResponseDTO> issuePlate(@Valid @RequestBody PlateRequestDTO dto) {
        log.info("Received issuePlate request");
        return ResponseEntity.ok(service.issuePlate(dto));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PlateResponseDTO>> listByOwner(@PathVariable UUID ownerId) {
        log.info("Received list plates for owner {}", ownerId);
        return ResponseEntity.ok(service.listByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlateResponseDTO> getById(@PathVariable UUID id) {
        log.info("Received get plate {}", id);
        return ResponseEntity.ok(service.getById(id));
    }
}
