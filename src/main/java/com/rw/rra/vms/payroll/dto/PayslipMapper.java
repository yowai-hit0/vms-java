package com.rw.rra.vms.payroll.dto;

import com.rw.rra.vms.payroll.Payslip;
import com.rw.rra.vms.users.DTO.UserMapper;
import com.rw.rra.vms.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PayslipMapper {
    PayslipMapper INSTANCE = Mappers.getMapper(PayslipMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    PayslipRequestDTO toRequestDTO(Payslip payslip);

    @Mapping(source = "employeeId", target = "employee")
    Payslip toEntity(PayslipRequestDTO dto);

    PayslipResponseDTO toResponseDTO(Payslip payslip);

    List<PayslipResponseDTO> toResponseDTOList(List<Payslip> payslips);

    default User map(UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        User user = new User();
        user.setId(employeeId);
        return user;
    }
}