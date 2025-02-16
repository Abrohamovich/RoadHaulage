package ua.ithillel.roadhaulage.controller.account;

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
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.util.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReportGeneratorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReportGeneratorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private ReportGenerator reportGenerator;

    private AuthUserDto authUserDto;
    private UserDto user;

    @BeforeEach
    void init(){
        authUserDto = new AuthUserDto();
        authUserDto.setId(1L);
        authUserDto.setRole(UserRole.USER);

        user = new UserDto();
        user.setId(1L);
        user.setRole(UserRole.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPhone("123456789");
        user.setIban("IBAN12345");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUserDto, null, authUserDto.getAuthorities())
        );
    }

    @Test
    void generateReport_success() throws Exception {
        OrderCategoryDto category = new OrderCategoryDto();
        category.setName("Category");

        OrderDto order = new OrderDto();
        order.setCategories(Set.of(category));
        order.setDepartureAddress(new AddressDto());
        order.setDeliveryAddress(new AddressDto());
        order.setWeight("2");
        order.setWeightUnit("kg");
        order.setCost("22");
        order.setCurrency("USD");
        order.setDimensions("2");
        order.setDimensionsUnit("cm");

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderService.findOrdersByCustomerId(anyLong()))
                .thenReturn(List.of(order, order));
        when(orderService.findOrdersByCourierId(anyLong()))
                .thenReturn(List.of(order));

        doNothing().when(reportGenerator).generateReport(any(UserDto.class), anyList(),
                        anyList(), any(ByteArrayOutputStream.class));
        mockMvc.perform(get("/generate-report"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"report.docx\""));

        verify(orderService, times(1))
                .findOrdersByCustomerId(anyLong());
        verify(orderService, times(1))
                .findOrdersByCourierId(anyLong());
        verify(reportGenerator, times(1))
                .generateReport(any(UserDto.class), anyList(), anyList(), any(ByteArrayOutputStream.class));
    }
}
