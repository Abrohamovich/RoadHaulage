package ua.ithillel.roadhaulage.controller.account.courier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.ithillel.roadhaulage.config.TestParent;
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AcceptedOrdersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AcceptedOrdersControllerTests extends TestParent {
    @MockitoBean
    private OrderService orderService;

    private UserDto user;

    @BeforeEach
    void init() {
        
        authUser.setId(1L);
        authUser.setRole(UserRole.USER);

        user = new UserDto();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
        );
    }

    @Test
    void acceptedOrdersPage() throws Exception {
        OrderCategoryDto category = new OrderCategoryDto();
        category.setName("Category");

        OrderDto order = new OrderDto();
        order.setStatus(OrderStatus.ACCEPTED);
        order.setCategories(Set.of(category));
        order.setDepartureAddress(new AddressDto());
        order.setDeliveryAddress(new AddressDto());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setCost("22");
        order.setCurrency("USD");
        order.setDimensions("2");
        order.setDimensionsUnit("cm");
        order.setCustomer(user);

        when(orderService.findOrdersByCourierIdAndStatus(
                1, OrderStatus.ACCEPTED, 0, 10
        )).thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(get("/account/delivered-orders/accepted/page=0"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/courier-orders/accepted"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void acceptOrder_success() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(OrderStatus.PUBLISHED);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted/page=0"));
    }

    @Test
    void acceptOrder_redirectError_orderStatusIsNotPublished() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(OrderStatus.CREATED);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void acceptOrder_orderIsNotPresent() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted/page=0"));
    }

    @Test
    void declineOrder_success() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCourier(user);
        orderDto.setStatus(OrderStatus.ACCEPTED);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted/page=0"));
    }

    @Test
    void declineOrder_redirectError_orderStatusIsNotAccepted() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCourier(user);
        orderDto.setStatus(OrderStatus.CREATED);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void declineOrder_orderIsNotPresent() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted/page=0"));
    }
}