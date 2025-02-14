package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByCustomerId(long id);
    List<Order> findOrdersByCourierId(long id);
}
