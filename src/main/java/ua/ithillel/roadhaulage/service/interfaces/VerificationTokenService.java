package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationTokenDto create(UserDto userDto, String token);
    void save(VerificationTokenDto token);
    Optional<VerificationTokenDto> getToken(String token);
    void delete(VerificationTokenDto token);
}
