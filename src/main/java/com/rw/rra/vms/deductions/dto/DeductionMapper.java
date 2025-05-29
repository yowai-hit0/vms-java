package com.rw.rra.vms.deductions.dto;

import com.rw.rra.vms.deductions.Deduction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeductionMapper {
    DeductionMapper INSTANCE = Mappers.getMapper(DeductionMapper.class);

    DeductionResponseDTO toResponseDTO(Deduction deduction);
    
    Deduction toEntity(DeductionRequestDTO dto);
    
    List<DeductionResponseDTO> toResponseDTOList(List<Deduction> deductions);
}