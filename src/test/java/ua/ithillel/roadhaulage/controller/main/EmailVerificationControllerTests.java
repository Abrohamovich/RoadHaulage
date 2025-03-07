package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ua.ithillel.roadhaulage.config.TestParent;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmailVerificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EmailVerificationControllerTests extends TestParent {

    @Test
    void verifyEmail_success_case0() throws Exception {
        String token = "token";

        when(userService.verifyEmail(token)).thenReturn((short) 0);

        mockMvc.perform(get("/verify-email")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "You have been successfully verified"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void verifyEmail_failure_case1() throws Exception {
        String token = "token";

        when(userService.verifyEmail(token)).thenReturn((short) 1);

        mockMvc.perform(get("/verify-email")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "This token does not exist, or this token is not yours"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void verifyEmail_success_case2() throws Exception {
        String token = "token";

        when(userService.verifyEmail(token)).thenReturn((short) 2);

        mockMvc.perform(get("/verify-email")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "There is no user with this token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void verifyEmail_failure_case3() throws Exception {
        String token = "token";

        when(userService.verifyEmail(token)).thenReturn((short) 3);

        mockMvc.perform(get("/verify-email")
                        .param("token", token))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage",
                        "Your token has expired"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}