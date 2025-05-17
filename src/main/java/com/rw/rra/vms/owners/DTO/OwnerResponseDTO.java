package com.rw.rra.vms.owners.DTO;

import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OwnerResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private AddressDTO address;
    private int vehicleCount;
    private int plateCount;
}
