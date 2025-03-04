package ua.ithillel.roadhaulage.controller.main;

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
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
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
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrdersPageControllerTests extends TestParent {
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private OrderCategoryService orderCategoryService;

    private OrderDto order;

    @BeforeEach
    void init() {

        authUser.setId(1L);
        authUser.setEmail("test@test.com");
        authUser.setRole(UserRole.USER);
        authUser.setEnabled(true);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setEnabled(true);
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
                new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
        );
    }

    @Test
    void ordersPage() throws Exception {
        when(orderCategoryService.findAll())
                .thenReturn(List.of());
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