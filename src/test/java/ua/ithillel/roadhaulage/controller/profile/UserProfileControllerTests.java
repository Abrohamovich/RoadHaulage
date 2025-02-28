package ua.ithillel.roadhaulage.controller.profile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserRatingService userRatingService;
    @MockitoBean
    private OrderService orderService;

    @Test
    void getUserProfileInfo_success() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneCode("1");
        user.setPhone("98565785");
        UserRatingDto userRating = new UserRatingDto();
        userRating.setId(1L);
        userRating.setAverage(3.3);
        userRating.setCount(4);

        when(userService.findByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(userRatingService.findByUserEmail(user.getEmail()))
                .thenReturn(Optional.of(userRating));

        mockMvc.perform(get("/user/john.doe@example.com/info"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("firstName", "lastName", "email",
                        "phone", "rating", "count"))
                .andExpect(view().name("profile/info"));
    }

    @Test
    void getUserProfileInfo_UserNotFound() throws Exception {
        String email = "unknown@example.com";
        when(userService.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(userRatingService.findByUserEmail(email))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/user/" + email +"/info"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void getUserProfileOrders_success() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneCode("1");
        user.setPhone("995251532");
        OrderDto order = new OrderDto();
        order.setCustomer(user);
        order.setStatus(OrderStatus.PUBLISHED);
        order.setCategories(Set.of(new OrderCategoryDto()));
        order.setDepartureAddress(new AddressDto());
        order.setDeliveryAddress(new AddressDto());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setDimensions("22");
        order.setDimensionsUnit("cm");
        order.setCost("3");
        order.setCurrency("USD");


        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(orderService.findOrdersByCustomerIdAndStatus(
                1L, OrderStatus.PUBLISHED, 0, 10)
        ).thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(get("/user/john.doe@example.com/orders/page=0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email", "orders",
                        "currentPage", "totalPages"))
                .andExpect(view().name("profile/orders"));
    }

    @Test
    void getUserProfileOrders_UserNotFound() throws Exception {
        String email = "unknown@example.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/" + email +"/orders/page=0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void getUserProfileOrderTest_success() throws Exception {
        UserDto user = new UserDto();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        OrderDto order = new OrderDto();
        order.setId(1L);
        order.setCustomer(user);
        order.setCategories(Set.of(new OrderCategoryDto()));
        order.setDepartureAddress(new AddressDto());
        order.setDeliveryAddress(new AddressDto());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setDimensions("22");
        order.setDimensionsUnit("cm");
        order.setCost("3");
        order.setCurrency("USD");
        order.setCreationDate(new Date(System.currentTimeMillis()));

        when(orderService.findById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/user/john.doe@example.com/order/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email", "order"))
                .andExpect(view().name("profile/order"));
    }

    @Test
    void getUserProfileOrder_UserNotFound() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/test@example.com/order/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
