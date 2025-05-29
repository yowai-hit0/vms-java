package com.rw.rra.vms.employment.dto;

import com.rw.rra.vms.employment.Employment;
import com.rw.rra.vms.users.User;
import com.rw.rra.vms.users.DTO.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EmploymentMapper {
    EmploymentMapper INSTANCE = Mappers.getMapper(EmploymentMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    EmploymentRequestDTO toRequestDTO(Employment employment);

    @Mapping(source = "employeeId", target = "employee")
    Employment toEntity(EmploymentRequestDTO dto);

    EmploymentResponseDTO toResponseDTO(Employment employment);

    List<EmploymentResponseDTO> toResponseDTOList(List<Employment> employments);

    default User map(UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        User user = new User();
        user.setId(employeeId);
        return user;
    }
}