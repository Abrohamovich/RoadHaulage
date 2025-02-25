package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationTokenDto save(VerificationTokenDto token);
    Optional<VerificationTokenDto> getToken(String token);
    Optional<VerificationTokenDto> findByUser(UserDto userDto);
    void delete(VerificationTokenDto token);
}
