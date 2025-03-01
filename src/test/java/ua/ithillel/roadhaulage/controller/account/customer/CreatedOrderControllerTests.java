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
//import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = CreatedOrderController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class CreatedOrderControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private OrderService orderService;
//    @MockitoBean
//    private UserRatingService userRatingService;
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
//    void createdOrdersPage() throws Exception {
//        OrderCategoryDto category = new OrderCategoryDto();
//        category.setName("Category");
//
//        OrderDto order = new OrderDto();
//        order.setStatus(OrderStatus.CREATED);
//        order.setCategories(Set.of(category));
//        order.setDepartureAddress(new AddressDto());
//        order.setDeliveryAddress(new AddressDto());
//        order.setWeight("2");
//        order.setWeightUnit("kg");
//        order.setCost("22");
//        order.setCurrency("USD");
//        order.setDimensions("2");
//        order.setDimensionsUnit("cm");
//
//        when(orderService.findOrdersByCustomerIdAndStatusNot(
//                1, OrderStatus.COMPLETED, 0, 10
//        )).thenReturn(new PageImpl<>(List.of(order)));
//
//        mockMvc.perform(get("/account/my-orders/created/page=0"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/customer-orders/created"))
//                .andExpect(model().attributeExists("orders"))
//                .andExpect(model().attributeExists("currentPage"))
//                .andExpect(model().attributeExists("totalPages"));
//    }
//
//    @Test
//    void publishOrder_success() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(user);
//        order.setStatus(OrderStatus.CREATED);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/created/publish")
//                .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/created/page=0"));
//    }
//
//    @Test
//    void publishOrder_failure_orderDoesNotBelongToCurrentUser() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setId(5L);
//
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(userDto);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/created/publish")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void publishOrder_failure_orderStatusIsNotCreatedNorChanged() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(user);
//        order.setStatus(OrderStatus.ACCEPTED);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/created/publish")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void publishOrder_doNone_orderIsNotPresent() throws Exception {
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/account/my-orders/created/publish")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/created/page=0"));
//
//        verify(orderService, times(0)).save(any());
//    }
//
//    @Test
//    void closeOrder() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(user);
//        order.setCourier(user);
//        order.setStatus(OrderStatus.ACCEPTED);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//        when(userRatingService.findById(anyLong()))
//                .thenReturn(Optional.of(mock(UserRatingDto.class)));
//
//        mockMvc.perform(post("/account/my-orders/created/close")
//                        .param("id", "1")
//                        .param("rating", "3.3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/completed/page=0"));
//
//        verify(orderService, times(1)).save(any());
//        verify(userRatingService, times(1)).update(any(), anyDouble());
//    }
//
//    @Test
//    void closeOrder_orderDoesNotBelongToCurrentUser() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setId(5L);
//
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(userDto);
//        order.setCourier(user);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/created/close")
//                        .param("id", "1")
//                        .param("rating", "3.3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void closeOrder_orderStatusIsNotAccepted() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(user);
//        order.setCourier(user);
//        order.setStatus(OrderStatus.CREATED);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/created/close")
//                        .param("id", "1")
//                        .param("rating", "3.3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void closeOrder_doNone_orderIsNotPresent() throws Exception {
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/account/my-orders/created/close")
//                        .param("id", "1")
//                        .param("rating", "3.3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/completed/page=0"));
//
//        verify(orderService, times(0)).save(any());
//        verify(userRatingService, times(0)).update(any(), anyDouble());
//    }
//
//    @Test
//    void deleteOrder() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(user);
//
//        when(orderService.findById(anyLong())).thenReturn(Optional.of(order));
//
//        mockMvc.perform(get("/account/my-orders/created/delete")
//                .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/created/page=0"));
//
//        verify(orderService, times(1)).delete(anyLong());
//    }
//
//    @Test
//    void deleteOrder_orderDoesNotBelongToCurrentUser() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setId(5L);
//
//        OrderDto order = new OrderDto();
//        order.setId(1L);
//        order.setCustomer(userDto);
//
//        when(orderService.findById(anyLong())).thenReturn(Optional.of(order));
//
//        mockMvc.perform(get("/account/my-orders/created/delete")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void deleteOrder_doNone() throws Exception {
//        when(orderService.findById(anyLong())).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/account/my-orders/created/delete")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/created/page=0"));
//    }
//}
