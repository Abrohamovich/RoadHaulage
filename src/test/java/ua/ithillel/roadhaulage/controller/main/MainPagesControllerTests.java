package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest(controllers = MainPagesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MainPagesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    public void aboutUsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about-us"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("about-us"));
    }

    @Test
    public void contactTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contact"));
    }

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    public void loginTest_withAttentionMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("attentionMessage", "Attention!"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("attentionMessage"))
                .andExpect(MockMvcResultMatchers.model().attribute("attentionMessage", "Attention!"));
    }

    @Test
    public void loginTest_withSuccessMessage() throws Exception  {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("successMessage", "Success!"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.model().attribute("successMessage", "Success!"));
    }

    @Test
    public void loginTest_withSuccessAndAttentionMessages() throws Exception  {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .param("successMessage", "Success!")
                        .param("attentionMessage", "Attention!"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("successMessage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("attentionMessage"))
                .andExpect(MockMvcResultMatchers.model().attribute("successMessage", "Success!"))
                .andExpect(MockMvcResultMatchers.model().attribute("attentionMessage", "Attention!"));
    }
}
