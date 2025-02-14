package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.mapper.OrderMapper;
import ua.ithillel.roadhaulage.repository.OrderRepository;

import java.sql.Date;
import java.util.*;

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
    void init(){
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
        order.setStatus(OrderStatus.CREATED);
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
    void save(){
        when(orderMapper.toEntity(orderDto)).thenReturn(order);

        orderServiceDefault.save(orderDto);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void delete(){
        orderServiceDefault.delete(order.getId());

        verify(orderRepository, times(1)).deleteById(order.getId());
    }

    @Test
    void updateOrder_success(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toEntity(orderDto)).thenReturn(order);

        orderServiceDefault.update(orderDto);

        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrder_notFound(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        orderServiceDefault.update(orderDto);

        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void findById_returnsPresentOptional(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        Optional<OrderDto> result = orderServiceDefault.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(orderDto, result.get());
    }

    @Test
    void findById_returnsEmptyOptional(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<OrderDto> result = orderServiceDefault.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsList(){
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result =  orderServiceDefault.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAll_returnsEmptyList(){
        when(orderRepository.findAll()).thenReturn(List.of());

        List<OrderDto> result =  orderServiceDefault.findAll();

        assertEquals(0, result.size());
    }

    @Test
    void findOrdersByCustomerId_returnsList(){
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result =  orderServiceDefault.findOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findOrdersByCustomerId_returnsEmptyList(){
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(List.of());

        List<OrderDto> result =  orderServiceDefault.findOrdersByCustomerId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findOrdersByCourierId_returnsList(){
        when(orderRepository.findOrdersByCourierId(anyLong())).thenReturn(List.of(order, order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result =  orderServiceDefault.findOrdersByCourierId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findOrdersByCourierId_returnsEmptyList(){
        when(orderRepository.findOrdersByCourierId(anyLong())).thenReturn(List.of());

        List<OrderDto> result =  orderServiceDefault.findOrdersByCourierId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void returnOtherPublishedOrders_returnsList(){
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PUBLISHED);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.PUBLISHED);
        Order order3 = new Order();
        order3.setId(3L);
        order3.setStatus(OrderStatus.PUBLISHED);
        List<Order> allOrders = List.of(order1, order2, order3);
        List<Order> customersOrders = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(customersOrders);

        List<OrderDto> result = orderServiceDefault.returnOtherPublishedOrders(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void returnOtherPublishedOrders_returnsEmptyList(){
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PUBLISHED);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.PUBLISHED);

        List<Order> allOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(orderRepository.findOrdersByCustomerId(anyLong())).thenReturn(allOrders);

        List<OrderDto> result = orderServiceDefault.returnOtherPublishedOrders(1L);

        assertEquals(0, result.size());
    }
}
