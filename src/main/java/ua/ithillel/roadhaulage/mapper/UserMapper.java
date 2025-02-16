package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    @Mapping(target = "customerOrders", ignore = true)
    @Mapping(target = "courierOrders", ignore = true)
    @Mapping(target = "rating", ignore = true)
    User toEntity(UserDto userDto);
    @Mapping(target = "authorities", ignore = true)
    AuthUserDto toAuthDto(User user);
}