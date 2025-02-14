//package ua.ithillel.roadhaulage.controller.account.customer;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.ithillel.roadhaulage.entity.*;
//import ua.ithillel.roadhaulage.service.interfaces.OrderService;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
//    private User user;
//
//    @BeforeEach
//    void init(){
//        user = new User();
//        user.setId(1L);
//        user.setRole(UserRole.USER);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("john@doe.com");
//        user.setPhone("123456789");
//        user.setIban("IBAN12345");
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
//        );
//    }
//
//    @Test
//    void completedOrdersPageTest() throws Exception {
//        OrderCategory category = new OrderCategory();
//        category.setName("Category");
//
//        Order order = new Order();
//        order.setStatus(OrderStatus.ACCEPTED);
//        order.setCategories(Set.of(category));
//        order.setDepartureAddress(new Address());
//        order.setDeliveryAddress(new Address());
//        order.setWeight("2");
//        order.setWeightUnit("kg");
//        order.setCost("22");
//        order.setCurrency("USD");
//        order.setDimensions("2");
//        order.setDimensionsUnit("cm");
//
//        when(orderService.findOrdersByCustomerId(anyLong())).thenReturn(List.of(order));
//
//        mockMvc.perform(get("/account/my-orders/completed"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/customer-orders/completed"))
//                .andExpect(model().attributeExists("orders"));
//    }
//
//}
