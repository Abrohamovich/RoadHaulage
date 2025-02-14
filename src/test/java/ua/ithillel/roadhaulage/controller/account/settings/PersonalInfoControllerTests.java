//package ua.ithillel.roadhaulage.controller.account.settings;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.ithillel.roadhaulage.entity.User;
//import ua.ithillel.roadhaulage.entity.UserRole;
//import ua.ithillel.roadhaulage.service.interfaces.UserService;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@WebMvcTest(controllers = PersonalInfoController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class PersonalInfoControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UserService userService;
//
//    @Test
//    void personalInfoPageTest() throws Exception {
//        User user = new User();
//        user.setRole(UserRole.USER);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPhone("123456789");
//        user.setIban("IBAN12345");
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
//        );
//
//        mockMvc.perform(get("/account/settings/personal-information"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/settings/personal-information"))
//                .andExpect(model().attributeExists("codes", "firstName",
//                        "lastName", "phone", "iban"));
//    }
//
//    @Test
//    void updateTest_success() throws Exception {
//        User user = new User();
//        user.setRole(UserRole.USER);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPhone("123456789");
//        user.setIban("IBAN12345");
//
//        when(userService.findByPhoneCodeAndPhone(anyString(), anyString()))
//                .thenReturn(Optional.empty());
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
//        );
//
//        mockMvc.perform(post("/account/settings/personal-information/update")
//                        .param("firstName", "JohnUpdated")
//                        .param("lastName", "DoeUpdated")
//                        .param("phoneCode", "1")
//                        .param("phone", "987654321")
//                        .param("iban", "IBAN67890"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/settings/personal-information"));
//
//    }
//
//    @Test
//    void updateTest_userWithPhoneAlreadyExists() throws Exception {
//        User user = new User();
//        user.setRole(UserRole.USER);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPhoneCode("1");
//        user.setPhone("123456789");
//        user.setIban("IBAN12345");
//
//        User existingUser = new User();
//        existingUser.setPhone("987654321");
//        existingUser.setPhoneCode("380");
//
//        when(userService.findByPhoneCodeAndPhone(anyString(), anyString()))
//                .thenReturn(Optional.of(existingUser));
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
//        );
//
//        mockMvc.perform(post("/account/settings/personal-information/update")
//                        .param("firstName", "")
//                        .param("lastName", "")
//                        .param("iban", "")
//                        .param("phoneCode", "380")
//                        .param("phone", "987654321"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/settings/personal-information"))
//                .andExpect(flash().attributeExists("phoneError"))
//                .andExpect(flash().attribute("phoneError", "Phone number already exists"));
//    }
//
//}
