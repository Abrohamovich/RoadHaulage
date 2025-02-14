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
//import ua.ithillel.roadhaulage.service.interfaces.UserService;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@WebMvcTest(controllers = EmailVerificationController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class EmailVerificationControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UserService userService;
//
//    @Test
//    void verifyEmailTests_success_case0() throws Exception {
//        String token = "token";
//
//        when(userService.verifyEmail(token)).thenReturn((short) 0);
//
//        mockMvc.perform(get("/verify-email")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "You have been successfully verified"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void verifyEmailTests_failure_case1() throws Exception {
//        String token = "token";
//
//        when(userService.verifyEmail(token)).thenReturn((short) 1);
//
//        mockMvc.perform(get("/verify-email")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "This token does not exist, or this token is not yours"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void verifyEmailTests_success_case2() throws Exception {
//        String token = "token";
//
//        when(userService.verifyEmail(token)).thenReturn((short) 2);
//
//        mockMvc.perform(get("/verify-email")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "There is no user with this token"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//    @Test
//    void verifyEmailTests_failure_case3() throws Exception {
//        String token = "token";
//
//        when(userService.verifyEmail(token)).thenReturn((short) 3);
//
//        mockMvc.perform(get("/verify-email")
//                        .param("token", token))
//                .andExpect(flash().attributeExists("successMessage"))
//                .andExpect(flash().attribute("successMessage",
//                        "Your token has expired"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//    }
//
//}