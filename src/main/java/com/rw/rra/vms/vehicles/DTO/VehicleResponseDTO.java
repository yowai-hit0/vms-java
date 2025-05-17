package com.rw.rra.vms.vehicles.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleResponseDTO {
    private UUID id;
    private String chassisNumber;
    private String manufacturerCompany;
    private Year manufactureYear;
    private double price;
    private String color;
    private String brand;
    private String modelName;
    private UUID ownerId;
    private UUID plateId;
    private LocalDateTime lastInspectionTime;
}
