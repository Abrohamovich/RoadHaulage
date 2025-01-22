package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.repository.OrderRepository;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    public void createOrderTest_returnsOrder() {

        Order mockOrder = mock(Order.class);

        Order result = orderService.createOrder(mockOrder.getCustomer(), OrderStatus.CREATED, mockOrder.getDeliveryAddress(),
                mockOrder.getDepartureAddress(), mockOrder.getAdditionalInfo(), mockOrder.getWeight(),
                mockOrder.getWeightUnit(), mockOrder.getDimensions(), mockOrder.getDimensionsUnit(),
                mockOrder.getCost(), mockOrder.getCurrency(), mockOrder.getCreationDate(), mockOrder.getCategories());

        assertNotNull(result);
        assertEquals(mockOrder.getCustomer(), result.getCustomer());
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(mockOrder.getDeliveryAddress(), result.getDeliveryAddress());
        assertEquals(mockOrder.getDepartureAddress(), result.getDepartureAddress());
        assertEquals(mockOrder.getAdditionalInfo(), result.getAdditionalInfo());
        assertEquals(mockOrder.getWeight(), result.getWeight());
        assertEquals(mockOrder.getWeightUnit(), result.getWeightUnit());
        assertEquals(mockOrder.getDimensions(), result.getDimensions());
        assertEquals(mockOrder.getDimensionsUnit(), result.getDimensionsUnit());
        assertEquals(mockOrder.getCost(), result.getCost());
        assertEquals(mockOrder.getCurrency(), result.getCurrency());
        assertEquals(mockOrder.getCreationDate(), result.getCreationDate());
        assertEquals(mockOrder.getCategories(), result.getCategories());
    }

    @Test
    public void updateOrderTest_success(){
        Order mockOrder = mock(Order.class);

        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        orderService.update(mockOrder);

        verify(orderRepository, times(1)).findById(mockOrder.getId());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    public void updateOrderTest_notFound(){
        Order mockOrder = mock(Order.class);

        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.empty());

        orderService.update(mockOrder);

        verify(orderRepository, times(1)).findById(mockOrder.getId());
        verify(orderRepository, never()).save(mockOrder);
    }

    @Test
    public void findByIdTest_returnsPresentOptional(){
        Order mockOrder = mock(Order.class);

        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        Optional<Order> orderOptional = orderService.findById(mockOrder.getId());
        assertTrue(orderOptional.isPresent());
        Order result = orderOptional.get();
        assertNotNull(result);
        assertEquals(mockOrder.getCustomer(), result.getCustomer());
        assertEquals(mockOrder.getDeliveryAddress(), result.getDeliveryAddress());
        assertEquals(mockOrder.getDepartureAddress(), result.getDepartureAddress());
        assertEquals(mockOrder.getAdditionalInfo(), result.getAdditionalInfo());
        assertEquals(mockOrder.getWeight(), result.getWeight());
        assertEquals(mockOrder.getWeightUnit(), result.getWeightUnit());
        assertEquals(mockOrder.getDimensions(), result.getDimensions());
        assertEquals(mockOrder.getDimensionsUnit(), result.getDimensionsUnit());
        assertEquals(mockOrder.getCost(), result.getCost());
        assertEquals(mockOrder.getCurrency(), result.getCurrency());
        assertEquals(mockOrder.getCreationDate(), result.getCreationDate());
        assertEquals(mockOrder.getCategories(), result.getCategories());

        verify(orderRepository, times(1)).findById(mockOrder.getId());
    }

    @Test
    public void findByIdTest_returnsEmptyOptional(){
        long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> orderOptional = orderService.findById(orderId);
        assertTrue(orderOptional.isEmpty());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void findAllTest_returnsList(){
        List<Order> mockOrders = Arrays.asList(mock(Order.class), mock(Order.class));
        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<Order> result =  orderService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void findAllTest_returnsEmptyList(){
        when(orderRepository.findAll()).thenReturn(List.of());

        List<Order> result =  orderService.findAll();

        assertEquals(0, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void findOrdersByCustomerIdTest_returnsList(){
        long customerId = 1L;
        List<Order> mockOrders = Arrays.asList(mock(Order.class), mock(Order.class));
        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(mockOrders);

        List<Order> result =  orderService.findOrdersByCustomerId(customerId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findOrdersByCustomerId(customerId);
    }

    @Test
    public void findOrdersByCustomerIdTest_returnsEmptyList(){
        long customerId = 1L;
        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(List.of());

        List<Order> result =  orderService.findOrdersByCustomerId(customerId);
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(orderRepository, times(1)).findOrdersByCustomerId(customerId);
    }

    @Test
    public void findOrdersByCourierIdTest_returnsList(){
        long customerId = 1L;
        List<Order> mockOrders = Arrays.asList(mock(Order.class), mock(Order.class));
        when(orderRepository.findOrdersByCourierId(customerId)).thenReturn(mockOrders);

        List<Order> result =  orderService.findOrdersByCourierId(customerId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findOrdersByCourierId(customerId);
    }

    @Test
    public void findOrdersByCourierIdTest_returnsEmptyList(){
        long customerId = 1L;
        when(orderRepository.findOrdersByCourierId(customerId)).thenReturn(List.of());

        List<Order> result =  orderService.findOrdersByCourierId(customerId);
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(orderRepository, times(1)).findOrdersByCourierId(customerId);
    }

    @Test
    public void deleteTest(){
        Order mockOrder = mock(Order.class);

        orderService.delete(mockOrder.getId());

        verify(orderRepository, times(1)).deleteById(mockOrder.getId());

    }

    @Test
    public void saveTest(){
        Order mockOrder = mock(Order.class);

        orderService.save(mockOrder);

        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    public void returnOtherPublishedOrdersTest_returnsList(){
        long customerId = 1L;

        Set<OrderCategory> categories = Set.of(mock(OrderCategory.class));
        OrderStatus status = OrderStatus.PUBLISHED;
        Address address = mock(Address.class);
        String additionalInfo = "additionalInfo";
        String weight = "3.1";
        String weightUnit = "kg";
        String dimensions = "23x23x23";
        String dimensionsUnit = "cm";
        String cost = "30.6";
        String currency = "EUR";
        Date createdAt = Date.valueOf("2016-01-01");

        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(status);
        order1.setDeliveryAddress(address);
        order1.setDepartureAddress(address);
        order1.setAdditionalInfo(additionalInfo);
        order1.setWeight(weight);
        order1.setWeightUnit(weightUnit);
        order1.setDimensions(dimensions);
        order1.setDimensionsUnit(dimensionsUnit);
        order1.setCost(cost);
        order1.setCurrency(currency);
        order1.setCreationDate(createdAt);
        order1.setCategories(categories);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(status);
        order2.setDeliveryAddress(address);
        order2.setDepartureAddress(address);
        order2.setAdditionalInfo(additionalInfo);
        order2.setWeight(weight);
        order2.setWeightUnit(weightUnit);
        order2.setDimensions(dimensions);
        order2.setDimensionsUnit(dimensionsUnit);
        order2.setCost(cost);
        order2.setCurrency(currency);
        order2.setCreationDate(createdAt);
        order2.setCategories(categories);
        Order order3 = new Order();
        order3.setId(3L);
        order3.setStatus(status);
        order3.setDeliveryAddress(address);
        order3.setDepartureAddress(address);
        order3.setAdditionalInfo(additionalInfo);
        order3.setWeight(weight);
        order3.setWeightUnit(weightUnit);
        order3.setDimensions(dimensions);
        order3.setDimensionsUnit(dimensionsUnit);
        order3.setCost(cost);
        order3.setCurrency(currency);
        order3.setCreationDate(createdAt);
        order3.setCategories(categories);


        List<Order> allOrders = Arrays.asList(order1, order2, order3);
        List<Order> customersOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(customersOrders);

        List<Order> result = orderService.returnOtherPublishedOrders(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(orderRepository, times(1)).findAll();
        verify(orderRepository, times(1)).findOrdersByCustomerId(customerId);

        assertTrue(result.contains(order3));
        assertFalse(result.contains(order1));
        assertFalse(result.contains(order2));
    }

    @Test
    public void returnOtherPublishedOrdersTest_returnsEmptyList(){
        long customerId = 1L;

        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        List<Order> allOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(allOrders);

        List<Order> result = orderService.returnOtherPublishedOrders(customerId);

        assertEquals(0, result.size());

        verify(orderRepository, times(1)).findAll();
        verify(orderRepository, times(1)).findOrdersByCustomerId(customerId);
    }

}
