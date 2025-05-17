package com.rw.rra.vms.owners.DTO;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OwnerRequestDTO {
    @NotBlank @Size(max = 50)
    private String firstName;

    @NotBlank @Size(max = 50)
    private String lastName;

    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phoneNumber;

    @NotBlank @Pattern(regexp = "\\d{16}", message = "National ID must be 16 digits")
    private String nationalId;

    @Valid @NotNull
    private AddressDTO address;
}
