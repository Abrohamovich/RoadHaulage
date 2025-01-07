package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void save(Order order);
    void delete(long id);
    List<Order> findOrdersByCustomerId(long id);
    List<Order> findOrdersByCourierId(long id);
    List<Order> findAll();
    void update(Order order);
    Optional<Order> findById(long id);
}
