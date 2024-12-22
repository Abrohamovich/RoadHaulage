package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;

public interface UserService{
    boolean save(User user);
    boolean delete(Long id);
}
