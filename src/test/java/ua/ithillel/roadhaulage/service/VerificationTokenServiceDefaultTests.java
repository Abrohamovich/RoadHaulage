package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.mapper.AddressMapper;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.mapper.VerificationTokenMapper;
import ua.ithillel.roadhaulage.repository.AddressRepository;
import ua.ithillel.roadhaulage.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceDefaultTests {
    @InjectMocks
    private VerificationTokenServiceDefault verificationTokenServiceDefault;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private VerificationTokenMapper verificationTokenMapper;
    @Mock
    private UserMapper usermapper;
    private VerificationTokenDto verificationTokenDto;
    private VerificationToken verificationToken;

    @BeforeEach
    void init(){
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        String token = UUID.randomUUID().toString();
        verificationTokenDto = new VerificationTokenDto();
        verificationTokenDto.setId(1L);
        verificationTokenDto.setUser(userDto);
        verificationTokenDto.setToken(token);
        verificationTokenDto.setExpiresAt(LocalDateTime.now());
        verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now());
    }

    @Test
    void save(){
        when(verificationTokenMapper.toEntity(verificationTokenDto)).thenReturn(verificationToken);
        when(verificationTokenRepository.save(verificationToken)).thenReturn(verificationToken);
        when(verificationTokenMapper.toDto(verificationToken)).thenReturn(verificationTokenDto);

        VerificationTokenDto result = verificationTokenServiceDefault.save(verificationTokenDto);

        assertNotNull(result);
        assertEquals(verificationTokenDto, result);

    }

    @Test
    void delete(){
        when(verificationTokenMapper.toEntity(verificationTokenDto)).thenReturn(verificationToken);
        verificationTokenServiceDefault.delete(verificationTokenDto);

        verify(verificationTokenRepository).delete(verificationToken);
    }

    @Test
    void getToken_returnsOptionalVerificationToken(){
        when(verificationTokenMapper.toDto(verificationToken)).thenReturn(verificationTokenDto);
        when(verificationTokenRepository.findByToken(anyString())).
                thenReturn(Optional.of(verificationToken));

        Optional<VerificationTokenDto> result = verificationTokenServiceDefault.getToken(anyString());

        assertTrue(result.isPresent());
        assertEquals(verificationTokenDto, result.get());
    }

    @Test
    void getToken_returnsEmptyOptional(){
        when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        Optional<VerificationTokenDto> result = verificationTokenServiceDefault.getToken(anyString());

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUser_returnsOptionalVerificationToken(){
        when(verificationTokenMapper.toDto(verificationToken)).thenReturn(verificationTokenDto);
        when(verificationTokenRepository.findByUser(any(User.class))).
                thenReturn(Optional.of(verificationToken));
        when(usermapper.toEntity(any(UserDto.class)))
                .thenReturn(mock(User.class));

        Optional<VerificationTokenDto> result = verificationTokenServiceDefault
                .findByUser(mock(UserDto.class));

        assertTrue(result.isPresent());
        assertEquals(verificationTokenDto, result.get());
    }

    @Test
    void findByUser_returnsEmptyOptional(){
        when(verificationTokenRepository.findByUser(any(User.class)))
                .thenReturn(Optional.empty());
        when(usermapper.toEntity(any(UserDto.class)))
                .thenReturn(mock(User.class));

        Optional<VerificationTokenDto> result = verificationTokenServiceDefault
                .findByUser(mock(UserDto.class));

        assertTrue(result.isEmpty());
    }
}