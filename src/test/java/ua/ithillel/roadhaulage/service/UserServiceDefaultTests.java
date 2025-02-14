//package ua.ithillel.roadhaulage.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import ua.ithillel.roadhaulage.entity.User;
//import ua.ithillel.roadhaulage.entity.VerificationToken;
//import ua.ithillel.roadhaulage.exception.UserCreateException;
//import ua.ithillel.roadhaulage.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class classUserServiceDefaultTests {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private VerificationTokenServiceDefault verificationTokenService;
//    @Mock
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    @InjectMocks
//    private UserServiceDefault userService;
//
//    @Test
//    void saveTest_success() {
//        User user = new User();
//        user.setPassword("plain1Password");
//        String encodedPassword = "encodedPassword";
//
//        when(bCryptPasswordEncoder.encode("plain1Password")).thenReturn(encodedPassword);
//
//        userService.save(user);
//
//        assertEquals(encodedPassword, user.getPassword());
//        verify(bCryptPasswordEncoder, times(1)).encode("plain1Password");
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void saveTest_throwsException_EmailAlreadyExists() {
//        String email = "test@example.com";
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword("plain1Password");
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode("+1");
//        user.setPhone("995251532");
//
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage().contains("A user with email " + email + " already exists"));
//    }
//
//    @Test
//    void saveTest_throwsException_PhoneAlreadyExists() {
//        String phoneCode = "380";
//        String phone = "991373605";
//        User user = new User();
//        user.setEmail("email");
//        user.setPassword("plain1Password");
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode(phoneCode);
//        user.setPhone(phone);
//
//
//        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage().contains("A user with phone " + phoneCode + phone + " already exists"));
//    }
//
//    @Test
//    void saveTest_throwsException_InvalidPassword() {
//        User user = new User();
//        user.setEmail("email");
//        user.setPassword("password");
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode("380");
//        user.setPhone("995251532");
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage()
//                .contains("Password must contain at least one digit and one uppercase"));
//    }
//
//    @Test
//    void saveTest_throwsException_PhoneAndEmailAlreadyExist() {
//        String email = "test@example.com";
//        String phoneCode = "380";
//        String phone = "991373605";
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword("plain1Password");
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode(phoneCode);
//        user.setPhone(phone);
//
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
//        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage().contains("A user with email " + email + " already exists." +
//                " A user with phone " + phoneCode + phone + " already exists"));
//    }
//
//    @Test
//    void saveTest_throwsException_EmailAlreadyExistsAndInvalidPassword() {
//        String email = "test@example.com";
//        String password = "invalidpassword";
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode("+1");
//        user.setPhone("995251532");
//
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage()
//                .contains("A user with email " + email + " already exists." +
//                        " Password must contain at least one digit and one uppercase"));
//    }
//
//    @Test
//    void saveTest_throwsException_PhoneAlreadyExistsAndInvalidPassword() {
//        String phoneCode = "380";
//        String phone = "991373605";
//        String password = "invalidpassword";
//        User user = new User();
//        user.setEmail("email");
//        user.setPassword(password);
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode(phoneCode);
//        user.setPhone(phone);
//
//        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage()
//                .contains("A user with phone " + phoneCode + phone + " already exists." +
//                        " Password must contain at least one digit and one uppercase"));
//    }
//
//    @Test
//    public void saveTest_throwsException_EmailAndPhoneAlreadyExistsAndInvalidPassword() {
//        String email = "test@example.com";
//        String phoneCode = "380";
//        String phone = "991373605";
//        String password = "invalidpassword";
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setFirstName("Test");
//        user.setLastName("User");
//        user.setPhoneCode(phoneCode);
//        user.setPhone(phone);
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
//        when(userRepository.findByPhoneCodeAndPhone(phoneCode, phone)).thenReturn(Optional.of(new User()));
//
//        UserCreateException exception = assertThrows(UserCreateException.class, () ->
//                userService.save(user));
//
//        assertTrue(exception.getMessage()
//                .contains("A user with email " + email + " already exists." +
//                        " A user with phone " + phoneCode + phone + " already exists." +
//                        " Password must contain at least one digit and one uppercase"));
//    }
//
//    @Test
//    void updateTest_withPasswordChanges() {
//        User user = new User();
//        user.setId(1L);
//        user.setPassword("newPassword");
//
//        User existingUser = new User();
//        existingUser.setId(1L);
//        existingUser.setPassword("oldPassword");
//
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));
//        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("codedPassword");
//
//        userService.update(user);
//
//        assertEquals("codedPassword", user.getPassword());
//        verify(userRepository, times(1)).save(user);
//        verify(userRepository, times(1)).findById(user.getId());
//    }
//
//    @Test
//    void updateTest_withoutPasswordChanges() {
//        User user = new User();
//        user.setId(1L);
//        user.setPassword("oldPassword");
//
//        User existingUser = new User();
//        existingUser.setId(1L);
//        existingUser.setPassword("oldPassword");
//
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));
//
//        userService.update(user);
//
//        assertEquals("oldPassword", user.getPassword());
//        verify(userRepository, times(1)).save(user);
//        verify(userRepository, times(1)).findById(user.getId());
//    }
//
//    @Test
//    void findByIdTest_returnsUserOptional() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
//
//        Optional<User> result = userService.findById(anyLong());
//
//        assertTrue(result.isPresent());
//        verify(userRepository, times(1)).findById(anyLong());
//
//    }
//
//    @Test
//    void findByIdTest_returnsEmptyOptional() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Optional<User> result = userService.findById(anyLong());
//
//        assertTrue(result.isEmpty());
//        verify(userRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    void findByEmailTest_returnsUserOptional() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));
//
//        Optional<User> result = userService.findByEmail(anyString());
//
//        assertTrue(result.isPresent());
//        verify(userRepository, times(1)).findByEmail(anyString());
//
//    }
//
//    @Test
//    void findByEmailTest_returnsEmptyOptional() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        Optional<User> result = userService.findByEmail(anyString());
//
//        assertTrue(result.isEmpty());
//        verify(userRepository, times(1)).findByEmail(anyString());
//    }
//
//    @Test
//    void findByPhoneCodeAndPhoneTest_returnsUserOptional() {
//        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.of(mock(User.class)));
//
//        Optional<User> result = userService.findByPhoneCodeAndPhone(anyString(), anyString());
//
//        assertTrue(result.isPresent());
//        verify(userRepository, times(1)).findByPhoneCodeAndPhone(anyString(), anyString());
//    }
//
//    @Test
//    void findByPhoneCodeAndPhoneTest_returnsEmptyOptional() {
//        when(userRepository.findByPhoneCodeAndPhone(anyString(), anyString())).thenReturn(Optional.empty());
//
//        Optional<User> result = userService.findByPhoneCodeAndPhone(anyString(), anyString());
//
//        assertTrue(result.isEmpty());
//        verify(userRepository, times(1)).findByPhoneCodeAndPhone(anyString(), anyString());
//    }
//
//    @Test
//    void verifyEmailTest_returnsZero_success() {
//        String token = UUID.randomUUID().toString();
//
//        User user = new User();
//        user.setId(1L);
//        user.setEnabled(false);
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(user);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyEmail(token);
//
//        assertEquals(0, result);
//        assertTrue(user.isEnabled());
//    }
//
//    @Test
//    void verifyEmailTest_returnsOne_verificationTokenIsEmpty_failure() {
//        String token = UUID.randomUUID().toString();
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());
//
//        short result = userService.verifyEmail(token);
//
//        assertEquals(1, result);
//    }
//
//    @Test
//    void verifyEmailTest_returnsOne_verificationTokenDoesNotBelongsToUser_failure() {
//        String token = UUID.randomUUID().toString();
//        String anotherToken = UUID.randomUUID().toString();
//
//        User user = new User();
//        user.setId(1L);
//        user.setEnabled(false);
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(anotherToken);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(user);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyEmail(token);
//
//        assertEquals(1, result);
//    }
//
//    @Test
//    void verifyEmailTest_returnsTwo_verificationTokenUserIsNull_failure() {
//        String token = UUID.randomUUID().toString();
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(null);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyEmail(token);
//
//        assertEquals(2, result);
//    }
//
//    @Test
//    void verifyEmailTest_returnsTwo_verificationTokenIsExpired_failure() {
//        String token = UUID.randomUUID().toString();
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));
//        verificationToken.setUser(mock(User.class));
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyEmail(token);
//
//        assertEquals(3, result);
//    }
//
//    @Test
//    void verifyPasswordTest_returnsZero_success() {
//        String token = UUID.randomUUID().toString();
//        String password = "NewPass_w0rd";
//
//        User user = new User();
//        user.setPassword("password1L");
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(user);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyPassword(token, password);
//
//        assertEquals(0, result);
//        assertNotEquals(password, user.getPassword());
//    }
//
//    @Test
//    void verifyPasswordTest_returnsOne_verificationTokenIsEmpty_failure() {
//        String token = UUID.randomUUID().toString();
//        String password = "NewPass_w0rd";
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());
//
//        short result = userService.verifyPassword(token, password);
//
//        assertEquals(1, result);
//    }
//
//    @Test
//    void verifyPasswordTest_returnsOne_verificationTokenDoesNotBelongsToUser_failure() {
//        String token = UUID.randomUUID().toString();
//        String anotherToken = UUID.randomUUID().toString();
//        String password = "NewPass_w0rd";
//
//        User user = mock(User.class);
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(anotherToken);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(user);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyPassword(token, password);
//
//        assertEquals(1, result);
//    }
//
//    @Test
//    void verifyPasswordTest_returnsTwo_verificationTokenUserIsNull_failure() {
//        String token = UUID.randomUUID().toString();
//        String password = "NewPass_w0rd";
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));
//        verificationToken.setUser(null);
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyPassword(token, password);
//
//        assertEquals(2, result);
//    }
//
//    @Test
//    void verifyPasswordTest_returnsTwo_verificationTokenIsExpired_failure() {
//        String token = UUID.randomUUID().toString();
//        String password = "NewPass_w0rd";
//
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setId(1L);
//        verificationToken.setToken(token);
//        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(20));
//        verificationToken.setUser(mock(User.class));
//
//        when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
//
//        short result = userService.verifyPassword(token, password);
//
//        assertEquals(3, result);
//    }
//
////    @Test
////    void loadUserByUsernameTest_returnsUser() {
////        User user = new User();
////        user.setEmail("test@gmail.com");
////        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
////
////        User result = userService.loadUserByUsername("test@gmail.com");
////
////        assertNotNull(result);
////        assertEquals(user.getEmail(), result.getEmail());
////    }
//
//    @Test
//    void loadUserByUsernameTest_throwsUsernameNotFoundException() {
//        String email = "test@gmail.com";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
//                () -> userService.loadUserByUsername(email));
//
//        assertEquals("Cant find user with username: " + email, exception.getMessage());
//    }
//}
