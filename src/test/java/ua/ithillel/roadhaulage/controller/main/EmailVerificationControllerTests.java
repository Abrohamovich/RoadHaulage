package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.ithillel.roadhaulage.service.UserServiceImp;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = EmailVerificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EmailVerificationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Configuration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Autowired
    private UserService userService;

    @Test
    public void verifyEmailTest_success() throws Exception {
        when(userService.verifyEmail(anyString())).thenReturn((short) 0);

        mockMvc.perform(MockMvcRequestBuilders.get("/verify-email")
                        .param("token", "8928fgwd-hf8239f-nf237gf3"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.flash()
                        .attribute("successMessage", "You have been successfully verified"));
    }

    @Test
    public void verifyEmailTest_failure_tokenDoesNotExistOrDoesNotBelongToUser() throws Exception {
        when(userService.verifyEmail(anyString())).thenReturn((short) 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/verify-email")
                        .param("token", "8928fgwd-hf8239f-nf237gf3"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.flash()
                        .attribute("successMessage",
                                "This token does not exist, or this token is not yours"));
    }

    @Test
    public void verifyEmailTest_failure_noUserDependsOnThisToken() throws Exception {
        when(userService.verifyEmail(anyString())).thenReturn((short) 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/verify-email")
                        .param("token", "8928fgwd-hf8239f-nf237gf3"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.flash()
                        .attribute("successMessage",
                                "This token does not exist, or this token is not yours"));
    }

    @Test
    public void verifyEmailTest_failure_tokenHasExpired() throws Exception {
        when(userService.verifyEmail(anyString())).thenReturn((short) 3);

        mockMvc.perform(MockMvcRequestBuilders.get("/verify-email")
                        .param("token", "8928fgwd-hf8239f-nf237gf3"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.flash()
                        .attribute("successMessage",
                                "Your token has expired"));
    }

}
