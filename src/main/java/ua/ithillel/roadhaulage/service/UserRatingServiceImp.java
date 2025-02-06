package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.UserRating;
import ua.ithillel.roadhaulage.repository.UserRatingRepository;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRatingServiceImp implements UserRatingService {
    private final UserRatingRepository repository;

    @Override
    public void save(UserRating userRating) {
        repository.save(userRating);
    }

    @Override
    public void update(UserRating userRating, double rating) {
        double average = userRating.getAverage();
        long total = userRating.getCount();
        average = Math.round(10.0 * (average * total + rating) / (total + 1)) / 10.0;
        userRating.setAverage(average);
        userRating.setCount(total + 1);
        repository.save(userRating);
    }

    @Override
    public Optional<UserRating> findById(long id) {
        return repository.findById(id);
    }
}
