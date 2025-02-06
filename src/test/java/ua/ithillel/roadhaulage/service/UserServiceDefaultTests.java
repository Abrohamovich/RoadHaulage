package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDefaultTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenServiceDefault verificationTokenService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserServiceDefault userService;

    @Test
    public void saveTest(){
        User user = new User();
        user.setPassword("plainPassword");
        String encodedPassword = "encodedPassword";

        when(bCryptPasswordEncoder.encode("plainPassword")).thenReturn(encodedPassword);

        userService.save(user);

        assertEquals(encodedPassword, user.getPassword());
        verify(bCryptPasswordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateTest_withPasswordChanges(){
        User user = new User();
        user.setId(1L);
        user.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("codedPassword");

        userService.update(user);

        assertEquals("codedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void updateTest_withoutPasswordChanges(){
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));

        userService.update(user);

        assertEquals("oldPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void findByIdTest_returnsUserOptional(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));

        Optional<User> result = userService.findById(anyLong());

        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findById(anyLong());

    }

    @Test
    public void findByIdTest_returnsEmptyOptional(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(anyLong());

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findByEmailTest_returnsUserOptional(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        Optional<User> result = userService.findByEmail(anyString());

        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @Test
    public void findByEmailTest_returnsEmptyOptional(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(anyString());

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void findByPhoneCodeAndPhoneTest_returnsUserOptional(){
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.of(mock(User.class)));

        Optional<User> result = userService.findByPhoneCodeAndPhone(anyString(), anyString());

        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByPhoneCodeAndPhone(anyString(), anyString());
    }

    @Test
    public void findByPhoneCodeAndPhoneTest_returnsEmptyOptional(){
        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findByPhoneCodeAndPhone(anyString(), anyString());

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByPhoneCodeAndPhone(anyString(), anyString());
    }

    @Test
    public void createUserTest_returnsUser_allFieldsAreValid(){
        String email = "test@example.com";
        String password = "Passw0rd";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneCode = "380";
        String phone = "990386970";
        boolean enabled = false;
        UserRole userRole = UserRole.USER;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.empty());

        User result = userService.createUser(email, password, firstName, lastName,
                phoneCode, phone, enabled, userRole);

        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(phone, result.getPhone());
        assertEquals(phoneCode, result.getPhoneCode());
        assertEquals(enabled, result.isEnabled());
        assertEquals(userRole, result.getRole());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).findByPhoneCodeAndPhone(phoneCode, phone);
    }

    @Test
    public void createUserTest_throwsException_EmailAlreadyExists(){
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser(email, "Password1", "John", "Doe",
                        "+1", "1234567890", true, UserRole.USER));

        assertTrue(exception.getMessage().contains("A user with email " + email + " already exists"));
    }

    @Test
    public void createUserTest_throwsException_PhoneAlreadyExists(){
        String phoneCode = "380";
        String phone = "991373605";

        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser("test@example.com", "Password1", "John", "Doe",
                        phoneCode, phone, true, UserRole.USER));

        assertTrue(exception.getMessage().contains("A user with phone " + phoneCode + phone + " already exists"));
    }

    @Test
    public void createUserTest_throwsException_InvalidPassword(){
        String password = "invalidpassword";

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser("test@example.com", password, "John", "Doe",
                        "380", "993732564", true, UserRole.USER));

        assertTrue(exception.getMessage()
                .contains("Password must contain at least one digit and one uppercase"));
    }

    @Test
    public void createUserTest_throwsException_PhoneAndEmailAlreadyExist(){
        String email = "test@example.com";
        String phoneCode = "380";
        String phone = "991373605";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser(email, "Password1", "John", "Doe",
                        phoneCode, phone, true, UserRole.USER));

        assertTrue(exception.getMessage().contains("A user with email " + email + " already exists." +
                " A user with phone " + phoneCode + phone + " already exists"));
    }

    @Test
    public void createUserTest_throwsException_EmailAlreadyExistsAndInvalidPassword(){
        String email = "test@example.com";
        String password = "invalidpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser(email, password, "John", "Doe",
                        "+1", "1234567890", true, UserRole.USER));

        assertTrue(exception.getMessage()
                .contains("A user with email " + email + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    public void createUserTest_throwsException_PhoneAlreadyExistsAndInvalidPassword(){
        String phoneCode = "380";
        String phone = "991373605";
        String password = "invalidpassword";

        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser("test@example.com", password, "John", "Doe",
                        phoneCode, phone, true, UserRole.USER));

        assertTrue(exception.getMessage()
                .contains("A user with phone " + phoneCode + phone + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    public void createUserTest_throwsException_EmailAndPhoneAlreadyExistsAndInvalidPassword(){
        String email = "test@example.com";
        String phoneCode = "380";
        String phone = "991373605";
        String password = "invalidpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));

        UserCreateException exception = assertThrows(UserCreateException.class, () ->
                userService.createUser(email, password, "John", "Doe",
                        phoneCode, phone, true, UserRole.USER));

        assertTrue(exception.getMessage()
                .contains("A user with email " + email + " already exists." +
                        " A user with phone " + phoneCode + phone + " already exists." +
                        " Password must contain at least one digit and one uppercase"));
    }

    @Test
    public void verifyEmailTest_returnsZero_success(){
        String token = UUID.randomUUID().toString();

        User user = new User();
        user.setId(1L);
        user.setEnabled(false);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(user);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyEmail(token);

        assertEquals(0, result);
        assertTrue(user.isEnabled());
    }

    @Test
    public void verifyEmailTest_returnsOne_verificationTokenIsEmpty_failure(){
        String token = UUID.randomUUID().toString();

        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

        short result = userService.verifyEmail(token);

        assertEquals(1, result);
    }

    @Test
    public void verifyEmailTest_returnsOne_verificationTokenDoesNotBelongsToUser_failure(){
        String token = UUID.randomUUID().toString();
        String anotherToken = UUID.randomUUID().toString();

        User user = new User();
        user.setId(1L);
        user.setEnabled(false);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(anotherToken);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(user);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyEmail(token);

        assertEquals(1, result);
    }

    @Test
    public void verifyEmailTest_returnsTwo_verificationTokenUserIsNull_failure(){
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(null);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyEmail(token);

        assertEquals(2, result);
    }

    @Test
    public void verifyEmailTest_returnsTwo_verificationTokenIsExpired_failure(){
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));
        verificationToken.setUser(mock(User.class));

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyEmail(token);

        assertEquals(3, result);
    }

    @Test
    public void verifyPasswordTest_returnsZero_success(){
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        User user = new User();
        user.setPassword("password1L");

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(user);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyPassword(token, password);

        assertEquals(0, result);
        assertNotEquals(password, user.getPassword());
    }

    @Test
    public void verifyPasswordTest_returnsOne_verificationTokenIsEmpty_failure(){
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

        short result = userService.verifyPassword(token, password);

        assertEquals(1, result);
    }

    @Test
    public void verifyPasswordTest_returnsOne_verificationTokenDoesNotBelongsToUser_failure(){
        String token = UUID.randomUUID().toString();
        String anotherToken = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        User user = mock(User.class);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(anotherToken);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(user);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyPassword(token, password);

        assertEquals(1, result);
    }

    @Test
    public void verifyPasswordTest_returnsTwo_verificationTokenUserIsNull_failure(){
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationToken.setUser(null);

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyPassword(token, password);

        assertEquals(2, result);
    }

    @Test
    public void verifyPasswordTest_returnsTwo_verificationTokenIsExpired_failure(){
        String token = UUID.randomUUID().toString();
        String password = "NewPass_w0rd";

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));
        verificationToken.setUser(mock(User.class));

        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

        short result = userService.verifyPassword(token, password);

        assertEquals(3, result);
    }

    @Test
    public void loadUserByUsernameTest_returnsUser(){
        User user = new User();
        user.setEmail("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        User result = userService.loadUserByUsername("test@gmail.com");

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void loadUserByUsernameTest_throwsUsernameNotFoundException(){
        String email = "test@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(email));

        assertEquals("Cant find user with username: " + email, exception.getMessage());
    }
}
