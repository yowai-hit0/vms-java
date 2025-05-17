package com.rw.rra.vms.plates;


import com.rw.rra.vms.plates.DTO.PlateRequestDTO;
import com.rw.rra.vms.plates.DTO.PlateResponseDTO;
import org.mapstruct.*;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PlateMapper {

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id",   ignore = true)
    @Mapping(target = "issuedDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "status",     constant = "AVAILABLE")
    PlateNumber toEntity(PlateRequestDTO dto);

    @Mapping(target = "ownerId",  source = "owner.id")
    @Mapping(target = "inUse",    expression = "java(p.getStatus()==PlateStatus.IN_USE)")
    PlateResponseDTO toDTO(PlateNumber p);
}
