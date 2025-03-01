package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;

public interface RegisterService {
    UserDto register(UserDto userDto);
    UserDto update(UserDto userDto);
}
