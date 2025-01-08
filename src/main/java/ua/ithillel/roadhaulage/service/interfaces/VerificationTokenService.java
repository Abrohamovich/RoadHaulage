package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationToken create(User user, String token);
    void save(VerificationToken token);
    Optional<VerificationToken> getToken(String token);
    void delete(VerificationToken token);
}
