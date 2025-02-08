package ua.ithillel.roadhaulage.controller.main;

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
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;


import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    private User user;

    @BeforeEach
    void init(){
        user = new User();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Test
    void ordersPaeTest() throws Exception {
        Order order = new Order();
        order.setCost("30");
        order.setCustomer(user);

        when(orderService.returnOtherPublishedOrders(anyLong()))
                .thenReturn(List.of(order));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("maxCost"))
                .andExpect(model().attributeExists("minCost"));
    }

    @Test
    void ordersPageTest_otherPublishedOrdersIsEmpty() throws Exception {
        user.setId(1L);
        user.setRole(UserRole.USER);

        when(orderService.returnOtherPublishedOrders(anyLong()))
                .thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    void filterTest() throws Exception {
        OrderCategory orderCategory = new OrderCategory();
        orderCategory.setName("Grocery");
        Order order = new Order();
        order.setCost("30");
        order.setCategories(Set.of(orderCategory));


        when(orderService.returnOtherPublishedOrders(anyLong()))
                .thenReturn(List.of(order, order));

        when(orderCategoryService.createOrderCategorySet(anyString()))
                .thenReturn(Set.of(orderCategory));

        mockMvc.perform(get("/orders/filter")
                        .param("currency", "ALL")
                        .param("categoriesString", "")
                        .param("max-cost", "0")
                        .param("min-cost", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("categoriesString"));
    }
}
