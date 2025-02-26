package ua.ithillel.roadhaulage.service.interfaces;

import org.springframework.data.domain.Page;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDto save(OrderDto orderDto);
    void delete(long id);
    List<OrderDto> findOrdersByCustomerId(long id);
    List<OrderDto> findOrdersByCourierId(long id);
    List<OrderDto> findAllPageable(int page, int pageSize);
    Optional<OrderDto> findById(long id);
    Page<OrderDto> findOrdersByCourierIdAndStatus(long id, OrderStatus status, int page, int size);
    Page<OrderDto> findOrdersByCustomerIdAndStatus(long id, OrderStatus status, int page, int size);
    Page<OrderDto> findOrdersByCustomerIdAndStatusNot(long id, OrderStatus status, int page, int size);
    Page<OrderDto> findOrdersByCustomerIdNotAndStatus(long id, OrderStatus status, int page, int size);
}
