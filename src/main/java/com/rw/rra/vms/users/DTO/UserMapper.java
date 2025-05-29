package com.rw.rra.vms.users.DTO;



import com.rw.rra.vms.users.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "verificationToken", ignore = true)
    @Mapping(target = "resetToken", ignore = true)
    User toEntity(RegisterRequestDTO userDto);

    UserResponseDTO toResponseDto(User user);
}
