package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.Order;

import java.util.List;

public interface OrderService {
    boolean save(Order order);
    void delete(long id);
    List<Order> findOrdersByCustomerId(long id);
    List<Order> findOrdersByCourierId(long id);
    List<Order> findAll();
    boolean update(Order order);
    Order findById(long id);
}
