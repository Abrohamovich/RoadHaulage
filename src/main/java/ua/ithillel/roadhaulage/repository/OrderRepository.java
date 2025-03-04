package ua.ithillel.roadhaulage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByCustomerId(long id);

    List<Order> findOrdersByCourierId(long id);

    Page<Order> findOrdersByCourierIdAndStatus(long id, OrderStatus status, Pageable pageable);

    Page<Order> findOrdersByCustomerIdAndStatus(long id, OrderStatus status, Pageable pageable);

    Page<Order> findOrdersByCustomerIdAndStatusNot(long id, OrderStatus status, Pageable pageable);

    Page<Order> findOrdersByCustomerIdNotAndStatus(long id, OrderStatus status, Pageable pageable);
}
