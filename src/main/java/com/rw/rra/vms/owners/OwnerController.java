package com.rw.rra.vms.owners;

import com.rw.rra.vms.owners.DTO.OwnerRequestDTO;
import com.rw.rra.vms.owners.DTO.OwnerResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/owners")
@Slf4j
@Validated
public class OwnerController {
    private final OwnerService service;

    public OwnerController(OwnerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OwnerResponseDTO> createOwner(
            @Valid @RequestBody OwnerRequestDTO dto) {
        log.info("Received createOwner request");
        OwnerResponseDTO resp = service.create(dto);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<Page<OwnerResponseDTO>> listOwners(
            Pageable pageable) {
        log.info("Received listOwners request");
        return ResponseEntity.ok(service.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> getById(@PathVariable UUID id) {
        log.info("Received getById for {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<OwnerResponseDTO> search(
            @RequestParam(required = false) String nationalId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {

        log.info("Received searchOwner request");
        if (nationalId != null) {
            return ResponseEntity.ok(service.getByNationalId(nationalId));
        }
        if (email != null) {
            return ResponseEntity.ok(service.getByEmail(email));
        }
        if (phone != null) {
            return ResponseEntity.ok(service.getByPhone(phone));
        }
        return ResponseEntity.badRequest().build();
    }
}
