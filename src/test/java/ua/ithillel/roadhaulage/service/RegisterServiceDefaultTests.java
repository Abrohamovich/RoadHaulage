package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceDefaultTests {
    @InjectMocks
    private RegisterServiceDefault registerServiceDefault;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
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

        UserDto result = registerServiceDefault.register(userDto);

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
                registerServiceDefault.register(userDto));

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
                registerServiceDefault.register(userDto));

        assertTrue(exception.getMessage().contains("A user with phone " + user.getPhoneCode() + user.getPhone() + " already exists"));
    }

    @Test
    void save_throwsException_InvalidPassword() {
        userDto.setPassword("password");
        user.setPassword("password");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                registerServiceDefault.register(userDto));

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
                registerServiceDefault.register(userDto));

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
                registerServiceDefault.register(userDto));

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
                registerServiceDefault.register(userDto));

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
                registerServiceDefault.register(userDto));

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

        registerServiceDefault.update(userDto);
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

        registerServiceDefault.update(userDto);
    }
}
