package ua.ithillel.roadhaulage.controller.account.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.ithillel.roadhaulage.config.TestParent;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonalInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PersonalInfoControllerTests extends TestParent {
    @MockitoBean
    private RegisterService registerService;

    private UserDto user;

    @BeforeEach
    void init() {

        authUser.setId(1L);
        authUser.setRole(UserRole.USER);

        user = new UserDto();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setLocalPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
        );
    }

    @Test
    void personalInfoPage() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/account/settings/personal-information"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/personal-information"))
                .andExpect(model().attributeExists("codes", "firstName",
                        "lastName", "localPhone", "iban"));
    }

    @Test
    void update_success() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByCountryCodeAndLocalPhone(anyString(), anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/account/settings/personal-information/update")
                        .param("firstName", "JohnUpdated")
                        .param("lastName", "DoeUpdated")
                        .param("countryCode", "1")
                        .param("localPhone", "987654321")
                        .param("iban", "IBAN67890"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/personal-information"));

        verify(registerService, times(1)).update(any(UserDto.class));
    }

    @Test
    void update_userWithPhoneAlreadyExists() throws Exception {
        UserDto existingUser = new UserDto();
        existingUser.setLocalPhone("123456789");
        existingUser.setCountryCode("380");

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.findByCountryCodeAndLocalPhone(anyString(), anyString()))
                .thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/account/settings/personal-information/update")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("iban", "")
                        .param("countryCode", "380")
                        .param("localPhone", "987654321"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/settings/personal-information"))
                .andExpect(flash().attributeExists("phoneError"))
                .andExpect(flash().attribute("phoneError", "Phone number already exists"));

        verify(registerService, times(0)).update(any(UserDto.class));
    }
}