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
//import ua.ithillel.roadhaulage.dto.*;
//import ua.ithillel.roadhaulage.entity.OrderStatus;
//import ua.ithillel.roadhaulage.entity.UserRole;
//import ua.ithillel.roadhaulage.exception.AddressCreateException;
//import ua.ithillel.roadhaulage.service.interfaces.AddressService;
//import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
//import ua.ithillel.roadhaulage.service.interfaces.OrderService;
//import ua.ithillel.roadhaulage.service.interfaces.UserService;
//
//import java.util.Optional;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = ChangeOrderController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//public class ChangeOrderControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UserService userService;
//    @MockitoBean
//    private OrderService orderService;
//    @MockitoBean
//    private OrderCategoryService orderCategoryService;
//    @MockitoBean
//    private AddressService addressService;
//
//    private UserDto user;
//    private OrderDto order;
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
//        OrderCategoryDto category = new OrderCategoryDto();
//        category.setName("Category");
//
//        order = new OrderDto();
//        order.setStatus(OrderStatus.PUBLISHED);
//        order.setCustomer(user);
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
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
//        );
//    }
//
//    @Test
//    void changePage_returnsChangePage() throws Exception {
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(get("/account/my-orders/change")
//                .param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/customer-orders/change"))
//                .andExpect(model().attributeExists("order"));
//    }
//
//    @Test
//    void changePage_redirectErrorPage_orderIsNotPresent() throws Exception {
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/account/my-orders/change")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void changePage_redirectErrorPage_orderDoesNotBelongToCurrentUser() throws Exception {
//        UserDto newUser = new UserDto();
//        newUser.setId(5L);
//        order.setCustomer(newUser);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(get("/account/my-orders/change")
//                        .param("id", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void changeOrder() throws Exception {
//        OrderDto order = new OrderDto();
//        order.setCustomer(user);
//        order.setStatus(OrderStatus.CREATED);
//
//        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//        when(orderCategoryService.createOrderCategorySet(anyString()))
//                .thenReturn(Set.of(mock(OrderCategoryDto.class)));
//        when(addressService.createAddress(anyString()))
//                .thenReturn(mock(AddressDto.class));
//
//        mockMvc.perform(post("/account/my-orders/change/edit")
//                .param("id", "1")
//                .param("categoriesString", "smth")
//                .param("cost", "smth")
//                .param("deliveryAddressString", "smth")
//                .param("departureAddressString", "smth")
//                .param("additionalInfo", "smth")
//                .param("weight", "smth")
//                .param("dimensions", "smth")
//                .param("weight-unit", "smth")
//                .param("dimensions-unit", "smth")
//                .param("currency", "smth"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/created/page=0"));
//
//        verify(orderCategoryService, times(1))
//                .createOrderCategorySet(anyString());
//        verify(addressService, times(2))
//                .createAddress(anyString());
//        verify(addressService, times(2))
//                .save(any(AddressDto.class));
//        verify(orderService, times(1))
//                .save(any(OrderDto.class));
//    }
//
//    @Test
//    void changeOrder_orderDoesNotBelongToCurrentUser() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setId(4L);
//
//        order.setCustomer(userDto);
//
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/change/edit")
//                        .param("id", "1")
//                        .param("categoriesString", "smth")
//                        .param("cost", "smth")
//                        .param("deliveryAddressString", "smth")
//                        .param("departureAddressString", "smth")
//                        .param("additionalInfo", "smth")
//                        .param("weight", "smth")
//                        .param("dimensions", "smth")
//                        .param("weight-unit", "smth")
//                        .param("dimensions-unit", "smth")
//                        .param("currency", "smth"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//
//    @Test
//    void changeOrder_throwsAddressCreateException() throws Exception {
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//        when(orderCategoryService.createOrderCategorySet(anyString()))
//                .thenReturn(Set.of(mock(OrderCategoryDto.class)));
//
//        when(addressService.createAddress(anyString()))
//                .thenThrow(new AddressCreateException("You didn't enter the address information correctly"));
//        mockMvc.perform(post("/account/my-orders/change/edit")
//                        .param("id", "1")
//                        .param("categoriesString", "smth")
//                        .param("cost", "smth")
//                        .param("deliveryAddressString", "smth")
//                        .param("departureAddressString", "smth")
//                        .param("additionalInfo", "smth")
//                        .param("weight", "smth")
//                        .param("dimensions", "smth")
//                        .param("weight-unit", "smth")
//                        .param("dimensions-unit", "smth")
//                        .param("currency", "smth"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/account/my-orders/create"))
//                .andExpect(flash().attribute("errorMessage", "You didn't enter the address information correctly"));
//    }
//
//    @Test
//    void changeOrder_orderStatusIsNotPublishedNorCreated() throws Exception {
//        order.setStatus(OrderStatus.COMPLETED);
//        when(orderService.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        mockMvc.perform(post("/account/my-orders/change/edit")
//                        .param("id", "1")
//                        .param("categoriesString", "smth")
//                        .param("cost", "smth")
//                        .param("deliveryAddressString", "smth")
//                        .param("departureAddressString", "smth")
//                        .param("additionalInfo", "smth")
//                        .param("weight", "smth")
//                        .param("dimensions", "smth")
//                        .param("weight-unit", "smth")
//                        .param("dimensions-unit", "smth")
//                        .param("currency", "smth"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/error"));
//    }
//}
