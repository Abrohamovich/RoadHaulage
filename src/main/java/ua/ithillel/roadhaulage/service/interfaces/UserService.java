package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;

public interface UserService{
    boolean save(User user);
    boolean delete(Long id);
    boolean update(User user);
    User findById(Long id);
    User findByEmail(String email);
    boolean verifyEmail(String token);
}
