package ua.ithillel.roadhaulage.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserApiControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    private static final String BASE_URL = "/admin/api/user";

    @BeforeEach
    void init(){
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(1L);
        authUserDto.setRole(UserRole.ADMIN);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
        );
    }

    @Test
    void findAll_returnsListOfUserDto() throws Exception {
        when(userService.findAllPageable(0, 10))
                .thenReturn(List.of(new UserDto(), new UserDto()));

        mockMvc.perform(get(BASE_URL + "/find-all")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findAll_returnsEmptyList() throws Exception {
        when(userService.findAllPageable(0, 10))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE_URL + "/find-all")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findBy_id_returnsUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(3L);

        when(userService.findById(anyLong()))
                .thenReturn(Optional.of(userDto));

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findBy_id_returnsNone() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findBy_email_returnsUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(3L);

        when(userService.findByEmail(anyString()))
                .thenReturn(Optional.of(userDto));

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findBy_email_returnsNone() throws Exception {
        when(userService.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("email", "test@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findBy_email_badRequest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("email", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findBy_id_badRequest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("id", ""))
                .andExpect(status().isBadRequest());
    }
}