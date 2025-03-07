package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByCountryCodeAndLocalPhone(String countryCode, String localPhone);
}
