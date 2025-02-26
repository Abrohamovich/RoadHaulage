package ua.ithillel.roadhaulage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByCustomerId(long id);
    List<Order> findOrdersByCourierId(long id);
    Page<Order> findOrdersByCustomerId(long id, Pageable pageable);
    Page<Order> findOrdersByCourierId(long id, Pageable pageable);
}
