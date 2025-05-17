package com.rw.rra.vms.plates.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlateRequestDTO {
    @NotNull
    private UUID ownerId;
    // allow override if you want custom plate, else service will generate
    private String plateNumber;
}
