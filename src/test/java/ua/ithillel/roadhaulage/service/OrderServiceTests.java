package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.mapper.OrderMapper;
import ua.ithillel.roadhaulage.repository.OrderRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @InjectMocks
    private OrderServiceDefault orderServiceDefault;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    private OrderDto orderDto;
    private Order order;

    @BeforeEach
    void init() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        Address address = new Address();
        address.setId(1L);
        OrderCategoryDto orderCategoryDto = new OrderCategoryDto();
        orderCategoryDto.setId(1L);
        OrderCategory orderCategory = new OrderCategory();
        orderCategory.setId(1L);
        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCustomer(userDto);
        orderDto.setStatus(OrderStatus.PUBLISHED);
        orderDto.setDeliveryAddress(addressDto);
        orderDto.setDepartureAddress(addressDto);
        orderDto.setAdditionalInfo("additionalInfo");
        orderDto.setWeight("3.5");
        orderDto.setWeightUnit("kg");
        orderDto.setDimensions("20x40x50");
        orderDto.setDimensionsUnit("cm");
        orderDto.setCost("5");
        orderDto.setCurrency("EUR");
        orderDto.setCreationDate(new Date(System.currentTimeMillis()));
        orderDto.setCategories(Set.of(orderCategoryDto));
        order = new Order();
        order.setId(1L);
        order.setCustomer(user);
        order.setStatus(OrderStatus.PUBLISHED);
        order.setDeliveryAddress(address);
        order.setDepartureAddress(address);
        order.setAdditionalInfo("additionalInfo");
        order.setWeight("3.5");
        order.setWeightUnit("kg");
        order.setDimensions("20x40x50");
        order.setDimensionsUnit("cm");
        order.setCost("5");
        order.setCurrency("EUR");
        order.setCreationDate(new Date(System.currentTimeMillis()));
        order.setCategories(Set.of(orderCategory));

    }

    @Test
    void save() {
        when(orderMapper.toEntity(orderDto)).thenReturn(order);

        orderServiceDefault.save(orderDto);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void delete() {
        orderServiceDefault.delete(order.getId());

        verify(orderRepository, times(1)).deleteById(order.getId());
    }

    @Test
    void findById_returnsPresentOptional() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        Optional<OrderDto> result = orderServiceDefault.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(orderDto, result.get());
    }

    @Test
    void findById_returnsEmptyOptional() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<OrderDto> result = orderServiceDefault.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findOrdersByCustomerId_returnsList() {
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderServiceDefault.findOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findOrdersByCustomerId_returnsEmptyList() {
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(List.of());

        List<OrderDto> result = orderServiceDefault.findOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findOrdersByCourierId_returnsList() {
        when(orderRepository.findOrdersByCourierId(anyLong())).thenReturn(List.of(order, order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderServiceDefault.findOrdersByCourierId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findOrdersByCourierId_returnsEmptyList() {
        when(orderRepository.findOrdersByCourierId(anyLong())).thenReturn(List.of());

        List<OrderDto> result = orderServiceDefault.findOrdersByCourierId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findAllPageable_returnsList() {
        when(orderRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of(order, order)));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderServiceDefault.findAllPageable(0, 1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllPageable_returnsEmptyList() {
        when(orderRepository.findAll(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of()));

        List<OrderDto> result = orderServiceDefault.findAllPageable(0, 1);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findOrdersByCourierIdAndStatus_returnsPage() {
        when(orderRepository.findOrdersByCourierIdAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(order, order)));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        Page<OrderDto> result = orderServiceDefault.findOrdersByCourierIdAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findOrdersByCourierIdAndStatus_returnsEmptyPage() {
        when(orderRepository.findOrdersByCourierIdAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<OrderDto> result = orderServiceDefault.findOrdersByCourierIdAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdAndStatus_returnsPage() {
        when(orderRepository.findOrdersByCustomerIdAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(order, order)));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdAndStatus_returnsEmptyPage() {
        when(orderRepository.findOrdersByCustomerIdAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdAndStatusNot_returnsPage() {
        when(orderRepository.findOrdersByCustomerIdAndStatusNot(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(order, order)));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdAndStatusNot(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdAndStatusNot_returnsEmptyPage() {
        when(orderRepository.findOrdersByCustomerIdAndStatusNot(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdAndStatusNot(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdNotAndStatus_returnsPage() {
        when(orderRepository.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(order, order)));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findOrdersByCustomerIdNotAndStatus_returnsEmptyPage() {
        when(orderRepository.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<OrderDto> result = orderServiceDefault.findOrdersByCustomerIdNotAndStatus(
                1, OrderStatus.PUBLISHED, 0, 10
        );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}