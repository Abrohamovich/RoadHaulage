package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDefaultTests {
    @InjectMocks
    private UserServiceDefault userServiceDefault;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private VerificationTokenServiceDefault verificationTokenService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void init(){
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setRole(UserRole.USER);
        userDto.setEnabled(false);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@doe.com");
        userDto.setPhoneCode("1");
        userDto.setPhone("123456789");
        userDto.setPassword("password1LdadL");
        userDto.setEnabled(true);
        user = new User();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setEnabled(false);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhoneCode("1");
        user.setPhone("123456789");
        user.setPassword("password1LdadL");
        user.setEnabled(true);
    }

    @Test
    void findById_returnsUserOptional() {
        when(userMapper.toDto(any(User.class))).thenReturn(mock(UserDto.class));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));

        Optional<UserDto> result = userServiceDefault.findById(anyLong());

        assertTrue(result.isPresent());
    }

    @Test
    void findById_returnsEmptyOptional() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<UserDto> result = userServiceDefault.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_returnsUserOptional() {
        when(userMapper.toDto(any(User.class))).thenReturn(mock(UserDto.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        Optional<UserDto> result = userServiceDefault.findByEmail(anyString());

        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @Test
    void findByEmail_returnsEmptyOptional() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<UserDto> result = userServiceDefault.findByEmail(anyString());

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void findByPhoneCodeAndPhone_returnsUserOptional() {
        when(userMapper.toDto(any(User.class))).thenReturn(mock(UserDto.class));
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.of(mock(User.class)));

        Optional<UserDto> result = userServiceDefault.findByPhoneCodeAndPhone(anyString(), anyString());

        assertTrue(result.isPresent());
        verify(userRepository, times(1))
                .findByPhoneCodeAndPhone(anyString(), anyString());
    }

    @Test
    void findByPhoneCodeAndPhone_returnsEmptyOptional() {
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<UserDto> result = userServiceDefault.findByPhoneCodeAndPhone(anyString(), anyString());

        assertTrue(result.isEmpty());
        verify(userRepository, times(1))
                .findByPhoneCodeAndPhone(anyString(), anyString());
    }

    @Test
    void findAllPageable_returnsFullList(){
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of(user)));

        List<UserDto> result = userServiceDefault.findAllPageable(0, 1);

        assertEquals(1, result.size());
        assertEquals(userDto, result.getFirst());
    }

    @Test
    void findAllPageable_returnsEmptyList(){
        when(userRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of()));

        List<UserDto> result = userServiceDefault.findAllPageable(0, 1);

        assertEquals(0, result.size());
    }

    @Test
    void verifyEmail_returnsZero_success() {
        String token = UUID.randomUUID().toString();
        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(userDto);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(verificationTokenService.getToken(anyString()))
                .thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyEmail(token);

        assertEquals(0, result);
        assertTrue(user.isEnabled());
    }

    @Test
    void verifyEmail_returnsOne_verificationTokenIsEmpty_failure() {
        String token = UUID.randomUUID().toString();

        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

        short result = userServiceDefault.verifyEmail(token);

        assertEquals(1, result);
    }

    @Test
    void verifyEmail_returnsOne_verificationTokenDoesNotBelongsToUser_failure() {
        String token = UUID.randomUUID().toString();
        String token1 = UUID.randomUUID().toString();
        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(userDto);
        VerificationTokenDto anotherVerificationToken = new VerificationTokenDto();
        anotherVerificationToken.setToken(token1);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(anotherVerificationToken));

        short result = userServiceDefault.verifyEmail(token);

        assertEquals(1, result);
    }

    @Test
    void verifyEmail_returnsTwo_verificationTokenUserIsNull_failure() {
        String token = UUID.randomUUID().toString();

        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(null);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyEmail(token);

        assertEquals(2, result);
    }

    @Test
    void verifyEmail_returnsTwo_verificationTokenIsExpired_failure() {
        String token = UUID.randomUUID().toString();

        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setUser(userDto);
        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyEmail(token);

        assertEquals(3, result);
    }

    @Test
    void verifyPassword_returnsOne_verificationTokenIsEmpty_failure() {
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

        short result = userServiceDefault.verifyPassword(token);

        assertEquals(1, result);
    }

    @Test
    void verifyPassword_returnsOne_verificationTokenDoesNotBelongsToUser_failure() {
        String token = UUID.randomUUID().toString();
        String token1 = UUID.randomUUID().toString();
        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(userDto);
        VerificationTokenDto anotherVerificationToken = new VerificationTokenDto();
        anotherVerificationToken.setToken(token1);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(anotherVerificationToken));

        short result = userServiceDefault.verifyPassword(token);

        assertEquals(1, result);
    }

    @Test
    void verifyPassword_returnsTwo_verificationTokenUserIsNull_failure() {
        String password = "NewPass_w0rd";
        String token = UUID.randomUUID().toString();
        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(null);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyPassword(token);

        assertEquals(2, result);
    }

    @Test
    void verifyPassword_returnsTwo_verificationTokenIsExpired_failure() {
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setUser(userDto);
        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyPassword(token);

        assertEquals(3, result);
    }

    @Test
    void loadUserByUsername_returnsUser() {
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setEmail("john@doe.com");
        when(userMapper.toAuthDto(user)).thenReturn(authUserDto);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        AuthUserDto result = userServiceDefault.loadUserByUsername("");

        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userServiceDefault.loadUserByUsername("email"));

        assertEquals("Cant find user with username: " + "email", exception.getMessage());
    }
}
