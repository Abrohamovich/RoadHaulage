package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.UserRating;

import java.util.Optional;

public interface UserRatingService {
    void save(UserRating userRating);
    void update(UserRating userRating, double rating);
    Optional<UserRating> findById(long id);
}
