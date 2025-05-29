package com.rw.rra.vms.messages.dto;

import com.rw.rra.vms.messages.Message;
import com.rw.rra.vms.users.DTO.UserMapper;
import com.rw.rra.vms.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    MessageRequestDTO toRequestDTO(Message message);

    @Mapping(source = "employeeId", target = "employee")
    Message toEntity(MessageRequestDTO dto);

    MessageResponseDTO toResponseDTO(Message message);

    List<MessageResponseDTO> toResponseDTOList(List<Message> messages);

    default User map(UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        User user = new User();
        user.setId(employeeId);
        return user;
    }
}