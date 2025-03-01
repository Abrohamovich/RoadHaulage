//package ua.ithillel.roadhaulage.controller.main;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = MainPagesController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class MainPagesControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void home() throws Exception {
//        mockMvc.perform(get("/home"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"));
//    }
//
//    @Test
//    void aboutUs() throws Exception {
//        mockMvc.perform(get("/about-us"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("about-us"));
//    }
//
//    @Test
//    void contact() throws Exception {
//        mockMvc.perform(get("/contact"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("contact"));
//    }
//
//    @Test
//    void login() throws Exception {
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"));
//    }
//
//    @Test
//    void login_withAttentionMessage() throws Exception {
//        mockMvc.perform(get("/login")
//                        .param("attentionMessage", "Attention!"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attributeExists("attentionMessage"))
//                .andExpect(model().attribute("attentionMessage", "Attention!"));
//    }
//
//    @Test
//    void login_withSuccessMessage() throws Exception  {
//        mockMvc.perform(get("/login")
//                        .param("successMessage", "Success!"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attributeExists("successMessage"))
//                .andExpect(model().attribute("successMessage", "Success!"));
//    }
//
//    @Test
//    void login_withSuccessAndAttentionMessages() throws Exception  {
//        mockMvc.perform(get("/login")
//                        .param("successMessage", "Success!")
//                        .param("attentionMessage", "Attention!"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attributeExists("successMessage"))
//                .andExpect(model().attributeExists("attentionMessage"))
//                .andExpect(model().attribute("successMessage", "Success!"))
//                .andExpect(model().attribute("attentionMessage", "Attention!"));
//    }
//}
