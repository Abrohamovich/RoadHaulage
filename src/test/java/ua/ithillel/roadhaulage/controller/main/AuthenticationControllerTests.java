package ua.ithillel.roadhaulage.controller.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.ithillel.roadhaulage.config.TestParent;
import ua.ithillel.roadhaulage.entity.AuthRequest;
import ua.ithillel.roadhaulage.entity.UserRole;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTests extends TestParent {
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void showLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("authRequest"))
                .andExpect(model().attributeExists("authRequest"));
    }

    @Test
    void showLoginPage_withAttentionMessage() throws Exception {
        mockMvc.perform(get("/login")
                        .flashAttr("attentionMessage", "Attention Message"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("attentionMessage"))
                .andExpect(model().attributeExists("authRequest"));
    }

    @Test
    void showLoginPage_withSuccessMessage() throws Exception {
        mockMvc.perform(get("/login")
                        .flashAttr("successMessage", "Success Message"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attributeExists("authRequest"));
    }

    @Test
    void login_success_redirectToHomePage() throws Exception {
        authUser.setId(1L);
        authUser.setRole(UserRole.USER);
        authUser.setEmail("test@test.com");
        authUser.setPassword("password");

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        authRequest.setPassword("password");

        String jwtToken = "mockJwtToken";

        when(userService.loadUserByUsername(authRequest.getEmail())).thenReturn(authUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtUtil.generateToken(authUser)).thenReturn(jwtToken);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", authRequest.getEmail())
                        .param("password", authRequest.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(cookie().exists("jwtToken"))
                .andExpect(cookie().value("jwtToken", jwtToken))
                .andExpect(cookie().httpOnly("jwtToken", true))
                .andExpect(cookie().path("jwtToken", "/"));
    }

    @Test
    void login_failure_redirectToLoginPage() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        authRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {
                });

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", authRequest.getEmail())
                        .param("password", authRequest.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("attentionMessage"))
                .andExpect(flash().attribute("attentionMessage", "Invalid credentials"));
    }
}