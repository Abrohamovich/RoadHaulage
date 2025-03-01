package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.UserRating;

import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    Optional<UserRating> findUserRatingByUserEmail(String email);
}
