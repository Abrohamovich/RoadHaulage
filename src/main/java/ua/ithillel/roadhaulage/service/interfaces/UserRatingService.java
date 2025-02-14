package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserRatingDto;

import java.util.Optional;

public interface UserRatingService {
    void save(UserRatingDto userRatingDto);
    void update(UserRatingDto userRating, double rating);
    Optional<UserRatingDto> findById(long id);
}
