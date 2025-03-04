package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.ithillel.roadhaulage.config.TestParent;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PasswordRecoveryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PasswordRecoveryControllerTests extends TestParent {
    @MockitoBean
    private RegisterService registerService;
    @MockitoBean
    private VerificationTokenService verificationTokenService;
    @MockitoBean
    private EmailService emailService;

    @Test
    void getPasswordRecoveryRequestPage() throws Exception {
        mockMvc.perform(get("/password-recovery"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("attentionMessage"))
                .andExpect(view().name("password-recovery"));
    }

    @Test
    void getPasswordRecoveryPage() throws Exception {
        mockMvc.perform(get("/password-recovery/change"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email"))
                .andExpect(view().name("change-password"));
    }

    @Test
    void changePassword_userIsPresent() throws Exception {
        String email = "test@example.com";
        String password = "newPassword123";
        UserDto userDto = new UserDto();
        userDto.setPassword(password);

        VerificationTokenDto tokenDto = new VerificationTokenDto();

        when(userService.findByEmail(email)).thenReturn(Optional.of(userDto));
        when(verificationTokenService.findByUser(userDto)).thenReturn(Optional.of(tokenDto));

        // Act & Assert
        mockMvc.perform(post("/password-recovery/change/confirm")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void changePassword_userIsNotPresent() throws Exception {
        String email = "nonexistent@example.com";
        String password = "newPassword123";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/password-recovery/change/confirm")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void recoverPasswordQues_success() throws Exception {
        String email = "test@test.com";
        String token = UUID.randomUUID().toString();
        UserDto mockUser = mock(UserDto.class);
        VerificationTokenDto verificationToken = mock(VerificationTokenDto.class);

        when(userService.findByEmail(email)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/password-recovery/confirm")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("attentionMessage",
                        "Please check your email confirm password recovery"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void recoverPasswordQues_failure_userNotFound() throws Exception {
        String email = "test@test.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/password-recovery/confirm")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("attentionMessage",
                        "User with email " + email + " not found"))
                .andExpect(redirectedUrl("/password-recovery"));
    }

    @Test
    void recoverPassword_success_case0() throws Exception {
        String token = UUID.randomUUID().toString();
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        VerificationTokenDto verToken = new VerificationTokenDto();
        verToken.setUser(userDto);
        verToken.setToken(token);

        when(userService.verifyPassword(token)).thenReturn((short) 0);
        when(verificationTokenService.getToken(token))
                .thenReturn(Optional.of(verToken));

        mockMvc.perform(get("/password-recovery/recover")
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("email"))
                .andExpect(redirectedUrl("/password-recovery/change"));
    }

    @Test
    void recoverPassword_failure_case1() throws Exception {
        String token = UUID.randomUUID().toString();

        when(userService.verifyPassword(anyString())).thenReturn((short) 1);
        when(verificationTokenService.getToken(token))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/password-recovery/recover")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "This token does not exist, or this token is not yours"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void recoverPassword_failure_case2() throws Exception {
        String token = UUID.randomUUID().toString();

        when(userService.verifyPassword(anyString())).thenReturn((short) 2);
        when(verificationTokenService.getToken(token))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/password-recovery/recover")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "There is no user with this token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void recoverPassword_failure_case3() throws Exception {
        String token = UUID.randomUUID().toString();

        when(userService.verifyPassword(anyString())).thenReturn((short) 3);
        when(verificationTokenService.getToken(token))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/password-recovery/recover")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "Your token has expired"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}