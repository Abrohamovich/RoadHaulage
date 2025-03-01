package ua.ithillel.roadhaulage.controller.account.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PrivacyController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PrivacyControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private RegisterService registerService;
    @MockitoBean
    private VerificationTokenService verificationTokenService;
    @MockitoBean
    private EmailService emailService;

    private UserDto user;

    @BeforeEach
    void init(){
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(1L);
        authUserDto.setRole(UserRole.USER);
        authUserDto.setEmail("john@doe.com");

        user = new UserDto();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
        );
    }

    @Test
    void privacyPageTest_withoutErrorParams() throws Exception {
        mockMvc.perform(get("/account/settings/privacy")
                .param("email", "john@doe.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/privacy"))
                .andExpect(model().attributeExists("email"));
    }

    @Test
    void privacyPageTest_withEmailErrorParam() throws Exception {
        mockMvc.perform(get("/account/settings/privacy")
                        .param("email", "john@doe.com")
                        .param("emailError", "Something went wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/privacy"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("emailError"));
    }

    @Test
    void privacyPageTest_withPasswordErrorParam() throws Exception {
        mockMvc.perform(get("/account/settings/privacy")
                        .param("email", "john@doe.com")
                        .param("passwordError", "Something went wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/privacy"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("passwordError"));
    }

    @Test
    void privacyPageTest_withBothErrorParams() throws Exception {
        mockMvc.perform(get("/account/settings/privacy")
                        .param("email", "john@doe.com")
                        .param("passwordError", "Something went wrong")
                        .param("emailError", "Something went wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/privacy"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("emailError"))
                .andExpect(model().attributeExists("passwordError"));
    }

    @Test
    void updatePrivacySettingsTest_updateAll() throws Exception {
        VerificationTokenDto verificationToken = new VerificationTokenDto();

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("email", "doe@john.com")
                        .param("password", "newPassword1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));

        verify(registerService, times(2)).update(user);
        verify(verificationTokenService, times(1)).save(any());
        verify(emailService, times(1)).sendEmailConfirmation(anyString(), anyString(), any());
    }

    @Test
    void updatePrivacySettingsTest_updatePassword() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("password", "newPassword1")
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/privacy"));

        verify(registerService, times(1)).update(user);
    }

    @Test
    void updatePrivacySettingsTest_updateEmail() throws Exception {
        VerificationTokenDto verificationToken = new VerificationTokenDto();

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("password", "")
                        .param("email", "doe@john@gmail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));

        verify(registerService, times(1)).update(user);
        verify(verificationTokenService, times(1)).save(any());
        verify(emailService, times(1)).sendEmailConfirmation(anyString(), anyString(), any());
    }

    @Test
    void updatePrivacySettingsTest_emailAndPasswordIsEmpty() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/privacy"));

    }

    @Test
    void updatePrivacySettingsTest_withBothErrorParams() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setEmail("existed@gmail.com");

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(newUser));

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("email", "existed@gmail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/privacy"))
                .andExpect(flash().attributeExists("passwordError"))
                .andExpect(flash().attributeExists("emailError"));

        verify(userService, times(2)).findByEmail(anyString());
    }

    @Test
    void updatePrivacySettingsTest_withPasswordErrorParam() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("email", "")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/privacy"))
                .andExpect(flash().attributeExists("passwordError"));

        verify(userService, times(1)).findByEmail(anyString());
    }

    @Test
    void updatePrivacySettingsTest_withEmailErrorParams() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setEmail("existed@gmail.com");

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(newUser));

        mockMvc.perform(post("/account/settings/privacy/update")
                        .param("email", "existed@gmail.com")
                        .param("password", "PAWffh#822bVBsf"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/privacy"))
                .andExpect(flash().attributeExists("emailError"));

        verify(userService, times(2)).findByEmail(anyString());
    }

}
