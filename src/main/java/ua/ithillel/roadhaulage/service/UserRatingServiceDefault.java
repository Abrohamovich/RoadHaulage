package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.UserRating;
import ua.ithillel.roadhaulage.mapper.UserRatingMapper;
import ua.ithillel.roadhaulage.repository.UserRatingRepository;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRatingServiceDefault implements UserRatingService {
    private final UserRatingRepository repository;
    private final UserRatingMapper userRatingMapper;

    @Override
    public UserRatingDto save(UserRatingDto userRatingDto) {
        log.info("Creating userRating for user with id : {}", userRatingDto.getUser().getId());
        UserRating saved = repository.save(userRatingMapper.toEntity(userRatingDto));
        return userRatingMapper.toDto(saved);
    }

    @Override
    public UserRatingDto update(UserRatingDto userRatingDto, double rating) {
        log.info("Update user rating from previous data, id: {}, average: {}, count: {}",
                userRatingDto.getAverage(), userRatingDto.getCount(), userRatingDto.getId());
        double average = userRatingDto.getAverage();
        long total = userRatingDto.getCount();
        average = Math.round(10.0 * (average * total + rating) / (total + 1)) / 10.0;
        userRatingDto.setAverage(average);
        userRatingDto.setCount(total + 1);
        UserRating updated = repository.save(userRatingMapper.toEntity(userRatingDto));
        log.info("Update user rating from previous data, id: {}, average: {}, count: {}",
                updated.getAverage(), updated.getCount(), updated.getId());
        return userRatingMapper.toDto(updated);
    }

    @Override
    public Optional<UserRatingDto> findById(long id) {
        log.info("Returning userRating for id : {}", id);
        return repository.findById(id)
                .map(userRatingMapper::toDto);
    }
}
