package com.rw.rra.vms.ownership.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferResponseDTO {
    private UUID id;
    private UUID vehicleId;
    private UUID fromOwnerId;
    private UUID toOwnerId;
    private UUID oldPlateId;
    private UUID newPlateId;
    private double transferAmount;
    private LocalDate transferDate;
}
