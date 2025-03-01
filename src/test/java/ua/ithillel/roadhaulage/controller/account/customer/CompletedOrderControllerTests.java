//package ua.ithillel.roadhaulage.controller.account.customer;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.ithillel.roadhaulage.dto.*;
//import ua.ithillel.roadhaulage.entity.OrderStatus;
//import ua.ithillel.roadhaulage.entity.UserRole;
//import ua.ithillel.roadhaulage.service.interfaces.OrderService;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = CompletedOrderController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class CompletedOrderControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private OrderService orderService;
//
//    private UserDto user;
//
//    @BeforeEach
//    void init(){
//        AuthUserDto authUserDto = new AuthUserDto();
//        authUserDto.setId(1L);
//        authUserDto.setRole(UserRole.USER);
//
//        user = new UserDto();
//        user.setId(1L);
//        user.setRole(UserRole.USER);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("john@doe.com");
//        user.setPhone("123456789");
//        user.setIban("IBAN12345");
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
//        );
//    }
//
//    @Test
//    void completedOrdersPage() throws Exception {
//        OrderCategoryDto category = new OrderCategoryDto();
//        category.setName("Category");
//
//        OrderDto order = new OrderDto();
//        order.setStatus(OrderStatus.COMPLETED);
//        order.setCategories(Set.of(category));
//        order.setDepartureAddress(new AddressDto());
//        order.setDeliveryAddress(new AddressDto());
//        order.setWeight("2");
//        order.setWeightUnit("kg");
//        order.setCost("22");
//        order.setCurrency("USD");
//        order.setDimensions("2");
//        order.setDimensionsUnit("cm");
//        order.setCourier(user);
//
//        when(orderService.findOrdersByCustomerIdAndStatus(
//                1, OrderStatus.COMPLETED, 0, 10
//        )).thenReturn(new PageImpl<>(List.of(order)));
//
//        mockMvc.perform(get("/account/my-orders/completed/page=0"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/customer-orders/completed"))
//                .andExpect(model().attributeExists("orders"))
//                .andExpect(model().attributeExists("currentPage"))
//                .andExpect(model().attributeExists("totalPages"));
//    }
//}
