package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.roadhaulage.entity.UserRating;
import ua.ithillel.roadhaulage.repository.UserRatingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRatingServiceDefaultTests {
    @InjectMocks
    private UserRatingServiceDefault service;
    @Mock
    private UserRatingRepository repository;

    @Test
    void saveTest() {
        service.save(mock(UserRating.class));

        verify(repository, times(1))
                .save(any(UserRating.class));
    }

    @Test
    void updateTest() {
        UserRating userRating = new UserRating();
        userRating.setId(1L);
        userRating.setAverage(4.3);
        userRating.setCount(2);

        service.update(userRating, 2.6);

        verify(repository, times(1))
                .save(any(UserRating.class));
    }

    @Test
    void findByIdTest_returnsPresentOptional() {
        UserRating userRating = new UserRating();
        userRating.setAverage(4.3);
        userRating.setCount(2);

        when(repository.findById(anyLong())).thenReturn(Optional.of(userRating));

        Optional<UserRating> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(userRating.getAverage(), result.get().getAverage());
        assertEquals(userRating.getCount(), result.get().getCount());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdTest_returnsEmptyOptional() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<UserRating> result = service.findById(1L);

        assertTrue(result.isEmpty());

        verify(repository, times(1)).findById(anyLong());
    }
}
