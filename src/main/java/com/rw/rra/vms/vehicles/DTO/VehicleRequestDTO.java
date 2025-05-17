package com.rw.rra.vms.vehicles.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Year;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleRequestDTO {
    @NotBlank @Size(max = 50)
    private String chassisNumber;

    @NotBlank @Size(max = 100)
    private String manufacturerCompany;

    @NotNull
    private Year manufactureYear;

    @Positive
    private double price;

    @NotBlank @Size(max = 30)
    private String color;

    @NotBlank @Size(max = 50)
    private String brand;

    @NotBlank @Size(max = 100)
    private String modelName;

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID plateId;
}
