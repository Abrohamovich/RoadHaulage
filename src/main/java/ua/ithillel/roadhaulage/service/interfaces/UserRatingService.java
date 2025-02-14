package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserRatingDto;

import java.util.Optional;

public interface UserRatingService {
    UserRatingDto save(UserRatingDto userRatingDto);
    UserRatingDto update(UserRatingDto userRating, double rating);
    Optional<UserRatingDto> findById(long id);
}
