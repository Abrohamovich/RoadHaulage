package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;

import java.util.Optional;

public interface UserService{
    void save(User user);
    void update(User user);
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    User createUser(String email, String password, String firstName,
                    String lastName, String phoneCode, String phone,
                    boolean enabled, UserRole userRole);
    short verifyEmail(String token);
    short verifyPassword(String token, String password);
}
