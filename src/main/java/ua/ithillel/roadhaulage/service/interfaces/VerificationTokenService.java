package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationTokenDto create(UserDto userDto, String token);
    VerificationTokenDto save(VerificationTokenDto token);
    Optional<VerificationTokenDto> getToken(String token);
    void delete(VerificationTokenDto token);
}
