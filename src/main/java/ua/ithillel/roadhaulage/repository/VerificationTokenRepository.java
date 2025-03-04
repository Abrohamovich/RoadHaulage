package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);
}
