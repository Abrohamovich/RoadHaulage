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
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

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
    private OrderService orderService;

    @BeforeEach
    void init(){
        UserDto user = new UserDto();
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

        when(orderService.returnOtherPublishedOrders(anyLong())).thenReturn(List.of(order));

        mockMvc.perform(get("/account/delivered-orders/accepted"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/courier-orders/accepted"))
                .andExpect(model().attributeExists("orders"));

    }

    @Test
    void acceptOrder() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(Optional.of(mock(OrderDto.class)));

        mockMvc.perform(post("/account/delivered-orders/accepted/accept")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));

        verify(orderService, times(1)).save(any(OrderDto.class));
    }

    @Test
    void declineOrder() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(Optional.of(mock(OrderDto.class)));

        mockMvc.perform(post("/account/delivered-orders/accepted/decline")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/delivered-orders/accepted"));

        verify(orderService, times(1)).save(any(OrderDto.class));
    }
}
