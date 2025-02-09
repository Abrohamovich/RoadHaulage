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
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ChangeOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ChangeOrderControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private OrderCategoryService orderCategoryService;
    @MockitoBean
    private AddressService addressService;

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
    void changePageTest_returnsChangePage() throws Exception {
        OrderCategory category = new OrderCategory();
        category.setName("Category");

        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        order.setCategories(Set.of(category));
        order.setDepartureAddress(new Address());
        order.setDeliveryAddress(new Address());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setCost("22");
        order.setCurrency("USD");
        order.setDimensions("2");
        order.setDimensionsUnit("cm");

        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(order));

        mockMvc.perform(get("/account/my-orders/change")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/customer-orders/change"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void changeOrderTest() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(Optional.of(mock(Order.class)));
        when(orderCategoryService.createOrderCategorySet(anyString()))
                .thenReturn(Set.of(mock(OrderCategory.class)));
        when(addressService.createAddress(anyString()))
                .thenReturn(Optional.of(mock(Address.class)));

        mockMvc.perform(post("/account/my-orders/change/edit")
                .param("id", "1")
                .param("categoriesString", "smth")
                .param("cost", "smth")
                .param("deliveryAddressString", "smth")
                .param("departureAddressString", "smth")
                .param("additionalInfo", "smth")
                .param("weight", "smth")
                .param("dimensions", "smth")
                .param("weight-unit", "smth")
                .param("dimensions-unit", "smth")
                .param("currency", "smth"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/created"));

        verify(orderCategoryService, times(1))
                .createOrderCategorySet(anyString());
        verify(addressService, times(2))
                .createAddress(anyString());
        verify(addressService, times(2))
                .save(any(Address.class));
        verify(orderService, times(1))
                .update(any(Order.class));
    }

    @Test
    void changeOrderTest_redirectError() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(orderCategoryService.createOrderCategorySet(anyString()))
                .thenReturn(Set.of(mock(OrderCategory.class)));
        when(addressService.createAddress(anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/account/my-orders/change/edit")
                        .param("id", "1")
                        .param("categoriesString", "smth")
                        .param("cost", "smth")
                        .param("deliveryAddressString", "smth")
                        .param("departureAddressString", "smth")
                        .param("additionalInfo", "smth")
                        .param("weight", "smth")
                        .param("dimensions", "smth")
                        .param("weight-unit", "smth")
                        .param("dimensions-unit", "smth")
                        .param("currency", "smth"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
