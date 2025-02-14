package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.UserRating;
import ua.ithillel.roadhaulage.mapper.UserRatingMapper;
import ua.ithillel.roadhaulage.repository.UserRatingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRatingServiceDefaultTests {
    @InjectMocks
    private UserRatingServiceDefault service;
    @Mock
    private UserRatingRepository repository;
    @Mock
    private UserRatingMapper userRatingMapper;
    private UserRatingDto userRatingDto;
    private UserRating userRating;

    @BeforeEach
    void init() {
        userRatingDto = new UserRatingDto();
        userRatingDto.setId(1L);
        userRatingDto.setAverage(4.5);
        userRatingDto.setCount(2);
        userRating = new UserRating();
        userRating.setId(1L);
        userRating.setAverage(4.5);
        userRating.setCount(2);
    }

    @Test
    void save_ShouldReturnSavedUserRatingDto() {
        when(userRatingMapper.toEntity(userRatingDto)).thenReturn(userRating);
        when(repository.save(userRating)).thenReturn(userRating);
        when(userRatingMapper.toDto(userRating)).thenReturn(userRatingDto);

        UserRatingDto result = service.save(userRatingDto);

        assertNotNull(result);
        assertEquals(userRatingDto, result);
        verify(repository).save(userRating);
    }

    @Test
    void update_ShouldReturnUpdatedUserRatingDto() {
        when(userRatingMapper.toEntity(any())).thenReturn(userRating);
        when(repository.save(userRating)).thenReturn(userRating);
        when(userRatingMapper.toDto(userRating)).thenReturn(userRatingDto);

        UserRatingDto result = service.update(userRatingDto, 5.0);

        assertNotNull(result);
        assertEquals(4.7, result.getAverage());
        assertEquals(3, result.getCount());
        verify(repository).save(any());
    }

    @Test
    void findById_ShouldReturnUserRatingDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(userRating));
        when(userRatingMapper.toDto(userRating)).thenReturn(userRatingDto);

        Optional<UserRatingDto> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(userRatingDto, result.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Optional<UserRatingDto> result = service.findById(2L);

        assertFalse(result.isPresent());
    }
}
