package ua.ithillel.roadhaulage.controller.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrdersPageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrdersPageControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private OrderCategoryService orderCategoryService;

    private OrderDto order;

    @BeforeEach
    void init(){
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(1L);
        authUserDto.setRole(UserRole.USER);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setRole(UserRole.USER);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        OrderCategoryDto orderCategory = new OrderCategoryDto();
        orderCategory.setName("Grocery");

        order = new OrderDto();
        order.setCost("30");
        order.setCategories(Set.of(orderCategory));
        order.setDeliveryAddress(new AddressDto());
        order.setDepartureAddress(new AddressDto());
        order.setCustomer(userDto);


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
        );
    }

    @Test
    void ordersPage() throws Exception {
        when(orderService.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, 0, 15
        )).thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(get("/orders/page=0"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("maxCost"))
                .andExpect(model().attributeExists("minCost"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void ordersPage_otherPublishedOrdersIsEmpty() throws Exception {
        when(orderService.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, 0, 15
        )).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/orders/page=0"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }
}