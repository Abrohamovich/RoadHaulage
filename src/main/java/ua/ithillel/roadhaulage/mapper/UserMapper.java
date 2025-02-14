package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}