package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.roadhaulage.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneCodeAndPhone(String phoneCode, String phone);
}
