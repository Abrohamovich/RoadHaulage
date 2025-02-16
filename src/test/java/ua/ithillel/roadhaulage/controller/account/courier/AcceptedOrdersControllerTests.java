package ua.ithillel.roadhaulage.controller.account.courier;

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
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AcceptedOrdersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AcceptedOrdersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OrderService orderService;

    private AuthUserDto authUserDto;
    private UserDto user;

    @BeforeEach
    void init(){
        authUserDto = new AuthUserDto();
        authUserDto.setId(1L);
        authUserDto.setRole(UserRole.USER);

        user = new UserDto();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
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

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.returnOtherPublishedOrders(anyLong())).thenReturn(List.of(order));

        mockMvc.perform(get("/account/delivered-orders/accepted"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/courier-orders/accepted"))
                .andExpect(model().attributeExists("orders"));

    }

    @Test
    void acceptOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));

        verify(orderService, times(1)).save(any(OrderDto.class));
    }

    @Test
    void acceptOrder_orderIsNotPresent() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));
    }

    @Test
    void declineOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCourier(user);

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));

        verify(orderService, times(1)).save(any(OrderDto.class));
    }

    @Test
    void declineOrder_orderIsNotPresent() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));
    }

    @Test
    void declineOrder_redirectError() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(5L);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCourier(userDto);

        when(userService.findById(anyLong())).thenReturn(Optional.of(userDto));
        when(orderService.findById(anyLong())).thenReturn(Optional.of(orderDto));

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
