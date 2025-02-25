package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService{
    UserDto save(UserDto userDto);
    UserDto update(UserDto userDto);
    Optional<UserDto> findById(long id);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findByPhoneCodeAndPhone(String phoneCode, String phone);
    List<UserDto> findAllPageable(int page, int pageSize);
    short verifyEmail(String token);
    short verifyPassword(String token);
}
