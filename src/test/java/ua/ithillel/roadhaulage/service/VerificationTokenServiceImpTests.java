package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceImpTests {
    @Mock
    private VerificationTokenRepository repository;
    @InjectMocks
    private VerificationTokenServiceImp service;

    @Test
    public void saveTest(){
        VerificationToken verificationToken = mock(VerificationToken.class);

        service.save(verificationToken);

        verify(repository).save(verificationToken);
    }

    @Test
    public void deleteTest(){
        VerificationToken verificationToken = mock(VerificationToken.class);

        service.delete(verificationToken);

        verify(repository).delete(verificationToken);
    }

    @Test
    public void getTokensTest_returnsOptionalVerificationToken(){
        VerificationToken verificationToken = mock(VerificationToken.class);
        when(repository.findByToken(anyString())).thenReturn(Optional.of(verificationToken));

        Optional<VerificationToken> result = service.getToken(anyString());

        assertTrue(result.isPresent());
        assertEquals(verificationToken, result.get());
        verify(repository, times(1)).findByToken(anyString());
    }

    @Test
    public void getTokensTest_returnsEmptyOptional(){
        when(repository.findByToken(anyString())).thenReturn(Optional.empty());

        Optional<VerificationToken> result = service.getToken(anyString());

        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByToken(anyString());
    }

    @Test
    public void createTest(){
        User user = mock(User.class);

        String token = "29384234-234234-234324";

        VerificationToken result = service.create(user, token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertEquals(user, result.getUser());
        assertTrue(result.getExpiresAt().isAfter(LocalDateTime.now()));
        assertTrue(result.getExpiresAt().isBefore(LocalDateTime.now().plusMinutes(21)));
    }

}