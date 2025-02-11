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
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = CreateNewOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CreateNewOrderControllerTests {
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
    void createPageTest() throws Exception {
        mockMvc.perform(get("/account/my-orders/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/customer-orders/create"))
                .andExpect(model().attributeExists("firstName"))
                .andExpect(model().attributeExists("lastName"))
                .andExpect(model().attributeExists("acceptDate"));
    }

    @Test
    void createOrderTest() throws Exception {
        when(orderCategoryService.createOrderCategorySet(anyString()))
                .thenReturn(Set.of(mock(OrderCategory.class)));
        when(addressService.createAddress(anyString()))
                .thenReturn((mock(Address.class)));

        mockMvc.perform(post("/account/my-orders/create/create")
                        .param("categoryNames", "Grocery")
                        .param("deliveryAddressString", "address")
                        .param("departureAddressString", "address")
                        .param("additionalInfo", "additional information")
                        .param("weight", "7.2")
                        .param("dimensions", "23x30x40")
                        .param("cost", "40")
                        .param("weight-unit", "kg")
                        .param("dimensions-unit", "cm")
                        .param("currency", "EUR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/create"));

        verify(orderCategoryService, times(1))
                .createOrderCategorySet(anyString());
        verify(orderCategoryService, times(1))
                .save(any(OrderCategory.class));
        verify(addressService, times(2))
                .createAddress(anyString());
        verify(addressService, times(2))
                .save(any(Address.class));
        verify(orderService, times(1))
                .save(any(Order.class));
    }

    @Test
    void createOrderTest_throwsAddressCreateException() throws Exception {
        when(orderCategoryService.createOrderCategorySet(anyString()))
                .thenReturn(Set.of(mock(OrderCategory.class)));

        doThrow(new AddressCreateException("You didn't enter the address information correctly"))
                .when(addressService).createAddress(anyString());

        mockMvc.perform(post("/account/my-orders/create/create")
                        .param("categoryNames", "Grocery")
                        .param("deliveryAddressString", "address")
                        .param("departureAddressString", "address")
                        .param("additionalInfo", "additional information")
                        .param("weight", "7.2")
                        .param("dimensions", "23x30x40")
                        .param("cost", "40")
                        .param("weight-unit", "kg")
                        .param("dimensions-unit", "cm")
                        .param("currency", "EUR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-orders/create"))
                .andExpect(flash().attribute("errorMessage", "You didn't enter the address information correctly"));

        verify(orderCategoryService, times(1))
                .createOrderCategorySet(anyString());
        verify(orderCategoryService, times(1))
                .save(any(OrderCategory.class));
        verify(addressService, times(1))
                .createAddress(anyString());
    }

}
