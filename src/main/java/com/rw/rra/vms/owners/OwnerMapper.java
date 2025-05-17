package com.rw.rra.vms.owners;

import com.rw.rra.vms.owners.DTO.OwnerRequestDTO;
import com.rw.rra.vms.owners.DTO.OwnerResponseDTO;
import com.rw.rra.vms.owners.DTO.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface OwnerMapper {

    Address toAddressEntity(AddressDTO dto);
    AddressDTO toAddressDTO(Address emb);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "plateNumbers", ignore = true)
    @Mapping(target = "transfersFrom", ignore = true)
    @Mapping(target = "transfersTo", ignore = true)
    Owner toEntity(OwnerRequestDTO dto);

    @Mapping(target = "vehicleCount",
            expression = "java(owner.getVehicles() != null ? owner.getVehicles().size() : 0)")
    @Mapping(target = "plateCount",
            expression = "java(owner.getPlateNumbers() != null ? owner.getPlateNumbers().size() : 0)")
    OwnerResponseDTO toDTO(Owner owner);
}
