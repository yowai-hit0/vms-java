package com.rw.rra.vms.ownership.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRequestDTO {
    @NotNull
    private UUID vehicleId;

    @NotNull
    private UUID newOwnerId;

    @NotNull
    private UUID newPlateId;

    @Positive(message = "Transfer amount must be positive")
    private double transferAmount;
}
