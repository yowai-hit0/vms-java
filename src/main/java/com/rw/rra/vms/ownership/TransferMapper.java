package com.rw.rra.vms.ownership;

import com.rw.rra.vms.ownership.DTO.TransferRequestDTO;
import com.rw.rra.vms.ownership.DTO.TransferResponseDTO;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromOwner", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "oldPlate", ignore = true)
    @Mapping(target = "toOwner", ignore = true)
    @Mapping(target = "newPlate", ignore = true)
    @Mapping(target = "transferDate", expression = "java(LocalDate.now())")
    OwnershipTransfer toEntity(TransferRequestDTO dto);

    @Mapping(target = "vehicleId",   source = "vehicle.id")
    @Mapping(target = "fromOwnerId", source = "fromOwner.id")
    @Mapping(target = "toOwnerId",   source = "toOwner.id")
    @Mapping(target = "oldPlateId",  source = "oldPlate.id")
    @Mapping(target = "newPlateId",  source = "newPlate.id")
    TransferResponseDTO toDTO(OwnershipTransfer trans);
}
