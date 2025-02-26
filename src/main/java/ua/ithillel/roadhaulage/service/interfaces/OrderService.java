package ua.ithillel.roadhaulage.service.interfaces;

import org.springframework.data.domain.Page;
import ua.ithillel.roadhaulage.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDto save(OrderDto orderDto);
    void delete(long id);
    List<OrderDto> findOrdersByCustomerId(long id);
    Page<OrderDto> findPageableOrdersByCustomerId(long id, int page, int size);
    List<OrderDto> findOrdersByCourierId(long id);
    Page<OrderDto> findPageableOrdersByCourierId(long id, int page, int size);
    List<OrderDto> findAllPageable(int page, int pageSize);
    Optional<OrderDto> findById(long id);
    List<OrderDto> returnOtherPublishedOrders(long id);
    Page<OrderDto> returnOtherPublishedPageableOrders(long id, int page, int size);
}
