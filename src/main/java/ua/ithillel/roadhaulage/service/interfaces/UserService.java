package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;

import java.util.Optional;

public interface UserService{
    void save(User user);
    void delete(Long id);
    void update(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    short verifyEmail(String token);
    short verifyPassword(String token, String password);
}
