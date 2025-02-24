package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.VerificationTokenDto;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationTokenDto save(VerificationTokenDto token);
    Optional<VerificationTokenDto> getToken(String token);
    void delete(VerificationTokenDto token);
}
