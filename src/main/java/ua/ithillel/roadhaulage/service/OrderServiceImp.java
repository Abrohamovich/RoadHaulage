package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.repository.OrderRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImp implements OrderService {
    private OrderRepository orderRepository;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void delete(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findOrdersByCustomerId(long id) {
        return orderRepository.findOrdersByCustomerId(id);
    }

    @Override
    public List<Order> findOrdersByCourierId(long id) {
        return orderRepository.findOrdersByCourierId(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void update(Order order) {
        Optional<Order> orderDB = orderRepository.findById(order.getId());
        if (orderDB.isPresent()) {
            orderRepository.save(order);
        }
    }

    @Override
    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }
}
