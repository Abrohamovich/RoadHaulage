package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void save(OrderDto orderDto);
    void delete(long id);
    List<OrderDto> findOrdersByCustomerId(long id);
    List<OrderDto> findOrdersByCourierId(long id);
    void update(OrderDto orderDto);
    Optional<OrderDto> findById(long id);
    List<OrderDto> returnOtherPublishedOrders(long id);

}
