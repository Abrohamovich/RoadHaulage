package ua.ithillel.roadhaulage.controller.account.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = CreatedOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CreatedOrderControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private UserRatingService userRatingService;

    private User user;

    @BeforeEach
    void init(){
        user = new User();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Test
    void createdOrdersPageTest() throws Exception {
        OrderCategory category = new OrderCategory();
        category.setName("Category");

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setCategories(Set.of(category));
        order.setDepartureAddress(new Address());
        order.setDeliveryAddress(new Address());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setCost("22");
        order.setCurrency("USD");
        order.setDimensions("2");
        order.setDimensionsUnit("cm");

        when(orderService.findOrdersByCustomerId(anyLong()))
                .thenReturn(List.of(order));

        mockMvc.perform(get("/account/my-orders/created"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/customer-orders/created"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void publishOrderTest() throws Exception {
        Order order = mock(Order.class);
        order.setId(1L);

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(order));

        mockMvc.perform(post("/account/my-orders/created/publish")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/created"));

        verify(orderService, times(1)).update(any());
    }

    @Test
    void publishOrderTest_doNone() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/account/my-orders/created/publish")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/created"));

        verify(orderService, times(0)).update(any());
    }

    @Test
    void closeOrderTest() throws Exception {
        User mockUser = new User();
        mockUser.setId(2L);

        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setCourier(mockUser);

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(mockOrder));

        when(userRatingService.findById(anyLong()))
                .thenReturn(Optional.of(mock(UserRating.class)));

        mockMvc.perform(post("/account/my-orders/created/close")
                        .param("id", "1")
                        .param("rating", "3.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/completed"));

        verify(orderService, times(1)).update(any());
        verify(userRatingService, times(1)).update(any(), anyDouble());
    }

    @Test
    void closeOrderTest_doNone() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/account/my-orders/created/close")
                        .param("id", "1")
                        .param("rating", "3.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/completed"));

        verify(orderService, times(0)).update(any());
        verify(userRatingService, times(0)).update(any(), anyDouble());
    }

    @Test
    void deleteOrderTest() throws Exception {
        mockMvc.perform(get("/account/my-orders/created/delete")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/created"));

        verify(orderService, times(1)).delete(anyLong());
    }
}
