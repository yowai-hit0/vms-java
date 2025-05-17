package com.rw.rra.vms.owners.DTO;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressDTO {
    @NotBlank @Size(max = 100)
    private String district;

    @NotBlank @Size(max = 100)
    private String province;

    @NotBlank @Size(max = 150)
    private String street;
}
