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
import ua.ithillel.roadhaulage.config.TestParent;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderApiControllerTests extends TestParent {
    private static final String BASE_URL = "/admin/api/order";
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrderService orderService;

    @BeforeEach
    void init() {
        
        authUser.setId(1L);
        authUser.setRole(UserRole.ADMIN);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
        );
    }

    @Test
    void findAll_returnsListOfOrderDto() throws Exception {
        when(orderService.findAllPageable(0, 10))
                .thenReturn(List.of(new OrderDto(), new OrderDto()));

        mockMvc.perform(get(BASE_URL + "/find-all")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findAll_returnsEmptyList() throws Exception {
        when(orderService.findAllPageable(0, 10))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE_URL + "/find-all")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returnsOrderDto() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(orderDto));

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returnsNone() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/find-by")
                        .param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_returnUpdatedOrderDto() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(orderDto));
        when(orderService.save(any(OrderDto.class)))
                .thenReturn(orderDto);

        mockMvc.perform(post(BASE_URL + "/update")
                        .content(objectMapper.writeValueAsString(orderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void update_returnNotFound() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post(BASE_URL + "/update")
                        .content(objectMapper.writeValueAsString(orderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}