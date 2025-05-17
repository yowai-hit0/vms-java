package com.rw.rra.vms.users.DTO;



import com.rw.rra.vms.users.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(RegisterRequestDTO userDto);
    UserResponseDTO toResponseDto(User user);
}