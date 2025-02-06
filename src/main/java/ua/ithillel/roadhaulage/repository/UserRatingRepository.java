package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.UserRating;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

}
