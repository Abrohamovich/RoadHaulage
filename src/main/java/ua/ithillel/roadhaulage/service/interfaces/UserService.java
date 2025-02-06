package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;

import java.util.Optional;

public interface UserService{
    void save(User user);
    void update(User user);
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneCodeAndPhone(String phoneCode, String phone);
    short verifyEmail(String token);
    short verifyPassword(String token, String password);
}
