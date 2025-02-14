package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;

import java.util.Optional;

public interface UserService{
    UserDto save(UserDto userDto);
    UserDto update(UserDto userDto);
    Optional<UserDto> findById(long id);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findByPhoneCodeAndPhone(String phoneCode, String phone);
    short verifyEmail(String token);
    short verifyPassword(String token, String password);
}
