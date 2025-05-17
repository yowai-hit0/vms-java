package com.rw.rra.vms.vehicles;

import com.rw.rra.vms.vehicles.DTO.VehicleRequestDTO;
import com.rw.rra.vms.vehicles.DTO.VehicleResponseDTO;
import org.mapstruct.*;
import java.time.Year;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "plateNumber", ignore = true)
    @Mapping(target = "lastInspectionTime", expression = "java(java.time.LocalDateTime.now())")
    Vehicle toEntity(VehicleRequestDTO dto);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "plateId", source = "plateNumber.id")
    VehicleResponseDTO toDTO(Vehicle v);
}
