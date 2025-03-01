package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RegisterService registerService;
    @MockitoBean
    private VerificationTokenService verificationTokenService;
    @MockitoBean
    private EmailService emailService;
    @MockitoBean
    private UserRatingService userRatingService;

    @Test
    void register() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("register"));
    }

    @Test
    void register_success() throws Exception {
        String email = "test@test.com";
        String firstName = "Test";
        String lastName = "Test";
        String phoneCode = "1";
        String phone = "995251532";
        String password = "Lolipop1Capop";
        String token = "test";

        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(false);
        user.setRole(UserRole.USER);
        user.setPhoneCode(phoneCode);
        user.setPhone(phone);
        user.setPassword(password);

        VerificationTokenDto verificationToken = new VerificationTokenDto();
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        when(registerService.register(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/register/reg")
                .param("email", email)
                .param("password", password)
                .param("countryCode", phoneCode)
                .param("phone", phone)
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void register_throwsUserCreateException() throws Exception {
        String email = "test@test.com";
        String password = "Lolipop1Capop";
        String phoneCode = "1";
        String phone = "995251532";
        String firstName = "Test";
        String lastName = "Test";

        doThrow(new UserCreateException("User creation failed"))
                .when(registerService).register(any(UserDto.class));

        mockMvc.perform(post("/register/reg")
                        .param("email", email)
                        .param("password", password)
                        .param("countryCode", phoneCode)
                        .param("phone", phone)
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attribute("errorMessage", "User creation failed"));
    }
}
