//package ua.ithillel.roadhaulage.controller.main;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.ithillel.roadhaulage.entity.User;
//import ua.ithillel.roadhaulage.entity.VerificationToken;
//import ua.ithillel.roadhaulage.service.interfaces.EmailService;
//import ua.ithillel.roadhaulage.service.interfaces.UserService;
//import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@WebMvcTest(controllers = PasswordRecoveryController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class PasswordRecoveryControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UserService userService;
//    @MockitoBean
//    private VerificationTokenService verificationTokenService;
//    @MockitoBean
//    private EmailService emailService;
//
//    @Test
//    void getPasswordRecoveryPageTest() throws Exception {
//        mockMvc.perform(get("/password-recovery"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("attentionMessage"))
//                .andExpect(view().name("password-recovery"));
//    }
//
//    @Test
//    void recoverPasswordQuesTest_success() throws Exception {
//        String email = "test@test.com";
//        String token = UUID.randomUUID().toString();
//        User mockUser = mock(User.class);
//        VerificationToken verificationToken = mock(VerificationToken.class);
//
//        when(userService.findByEmail(email)).thenReturn(Optional.of(mockUser));
//        when(verificationTokenService.create(mockUser, token)).thenReturn(verificationToken);
//
//        mockMvc.perform(post("/password-recovery/confirm")
//                        .param("email", email))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(flash().attribute("attentionMessage",
//                        "Please check your email confirm password recovery"))
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void recoverPasswordQuesTest_failure_userNotFound() throws Exception {
//        String email = "test@test.com";
//
//        when(userService.findByEmail(email)).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/password-recovery/confirm")
//                        .param("email", email))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(flash().attribute("attentionMessage",
//                        "User with email " + email + " not found"))
//                .andExpect(redirectedUrl("/password-recovery"));
//    }
//
//    @Test
//    void recoverPasswordTests_success_case0() throws Exception {
//        String token = UUID.randomUUID().toString();
//
//        when(userService.verifyPassword(anyString(), anyString())).thenReturn((short) 0);
//
//        mockMvc.perform(get("/password-recovery/recover")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "Your password has been successfully changed"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void recoverPasswordTests_failure_case1() throws Exception {
//        String token = UUID.randomUUID().toString();
//
//        when(userService.verifyPassword(anyString(), anyString())).thenReturn((short) 1);
//
//        mockMvc.perform(get("/password-recovery/recover")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "This token does not exist, or this token is not yours"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void recoverPasswordTests_failure_case2() throws Exception {
//        String token = UUID.randomUUID().toString();
//
//        when(userService.verifyPassword(anyString(), anyString())).thenReturn((short) 2);
//
//        mockMvc.perform(get("/password-recovery/recover")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "There is no user with this token"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void recoverPasswordTests_failure_case3() throws Exception {
//        String token = UUID.randomUUID().toString();
//
//        when(userService.verifyPassword(anyString(), anyString())).thenReturn((short) 3);
//
//        mockMvc.perform(get("/password-recovery/recover")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "Your token has expired"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//}
