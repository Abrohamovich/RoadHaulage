package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
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
    void save_success() {
        userDto.setPassword("ENCODED_PASSWORD");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("ENCODED_PASSWORD");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userServiceDefault.save(userDto);

        assertNotNull(result);
        assertEquals("ENCODED_PASSWORD", result.getPassword());
    }

    @Test
    void save_throwsException_EmailAlreadyExists() {
        userDto.setPassword("plain1Password");
        user.setPassword("plain1Password");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage().contains("A user with email " + user.getEmail() + " already exists"));
    }

    @Test
    void save_throwsException_PhoneAlreadyExists() {
        String password = "plain1Password";
        userDto.setPhoneCode("1");
        userDto.setPhone("123456789");
        userDto.setPassword(password);
        user.setPhoneCode("1");
        user.setPhone("123456789");
        user.setPassword(password);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByPhoneCodeAndPhone(user.getPhoneCode(), user.getPhone()))
                .thenReturn(Optional.of(mock(User.class)));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage().contains("A user with phone " + user.getPhoneCode() + user.getPhone() + " already exists"));
    }

    @Test
    void save_throwsException_InvalidPassword() {
        userDto.setPassword("password");
        user.setPassword("password");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage()
                .contains("Password must contain at least one digit and one uppercase"));
    }

    @Test
    void save_throwsException_PhoneAndEmailAlreadyExist() {
        String phoneCode = "1";
        String phone = "123456789";
        String email = "test@example.com";
        String password = "plain1Password";
        userDto.setPhoneCode(phoneCode);
        userDto.setPhone(phone);
        userDto.setEmail(email);
        userDto.setPassword(password);
        user.setPhoneCode(phoneCode);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByEmail(anyString())).
                thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString()))
                .thenReturn(Optional.of(mock(User.class)));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage().contains("A user with email " + email + " already exists." +
                " A user with phone " + phoneCode + phone + " already exists"));
    }

    @Test
    void save_throwsException_EmailAlreadyExistsAndInvalidPassword() {
        String email = "test@example.com";
        String password = "password";
        user.setEmail(email);
        user.setPassword(password);
        userDto.setEmail(email);
        user.setPassword(password);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage()
                .contains("A user with email " + email + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    void save_throwsException_PhoneAlreadyExistsAndInvalidPassword() {
        String phoneCode = "1";
        String phone = "123456789";
        String password = "password";
        user.setPhone(phone);
        user.setPhoneCode(phoneCode);
        user.setPassword(password);
        userDto.setPhoneCode(phoneCode);
        userDto.setPhone(phone);
        user.setPassword(password);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString()))
                .thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage()
                .contains("A user with phone " + phoneCode + phone + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    void save_throwsException_EmailAndPhoneAlreadyExistsAndInvalidPassword() {
        String email = "test@example.com";
        String phoneCode = "1";
        String phone = "123456789";
        String password = "password";
        user.setEmail(email);
        user.setPhone(phone);
        user.setPhoneCode(phoneCode);
        user.setPassword(password);
        userDto.setEmail(email);
        userDto.setPhoneCode(phoneCode);
        userDto.setPhone(phone);
        user.setPassword(password);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone))
                .thenReturn(Optional.of(mock(User.class)));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userServiceDefault.save(userDto));

        assertTrue(exception.getMessage()
                .contains("A user with email " + email + " already exists." +
                        " A user with phone " + phoneCode + phone + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    void update_withoutPasswordChanges(){
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any())).thenReturn(userDto);

        userServiceDefault.update(userDto);
    }

    @Test
    void update_withPasswordChanges(){
        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setPassword("oaiwhdOI(#R#(WQHf3wf");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(userFromDB));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("OQ*#U(*#QYF(F(F(GF");
        when(userMapper.toDto(any())).thenReturn(userDto);

        userServiceDefault.update(userDto);
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

        Optional<UserDto> result = userServiceDefault.findById(anyLong());

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
    void verifyPassword_returnsZero_success() {
        String token = UUID.randomUUID().toString();
        String password = "password1LdadL";

        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(userDto);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(verificationTokenService.getToken(anyString()))
                .thenReturn(Optional.of(verificationToken));

        short result = userServiceDefault.verifyPassword(token, password);

        assertEquals(0, result);
    }

    @Test
    void verifyPassword_returnsOne_verificationTokenIsEmpty_failure() {
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

        short result = userServiceDefault.verifyPassword(token, password);

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

        short result = userServiceDefault.verifyPassword(token, "password");

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

        short result = userServiceDefault.verifyPassword(token, password);

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

        short result = userServiceDefault.verifyPassword(token, password);

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
