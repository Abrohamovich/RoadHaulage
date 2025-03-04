package ua.ithillel.roadhaulage.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.ithillel.roadhaulage.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<UserDto> findById(long id);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findByCountryCodeAndLocalPhone(String countryCode, String localPhone);

    List<UserDto> findAllPageable(int page, int pageSize);

    short verifyEmail(String token);

    short verifyPassword(String token);
}
