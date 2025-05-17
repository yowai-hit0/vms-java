package com.rw.rra.vms.plates.DTO;

import com.rw.rra.vms.plates.PlateStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlateResponseDTO {
    private UUID id;
    private String plateNumber;
    private LocalDate issuedDate;
    private PlateStatus status;
    private UUID ownerId;
    private boolean inUse;
    // convenience, add vehicle if available
}
