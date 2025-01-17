package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.repository.OrderRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public List<Order> returnOtherPublishedOrders(long id) {
        List<Order> allOrders = findAll();
        List<Order> customerOrders = findOrdersByCustomerId(id);
        List<Order> orders = allOrders.stream()
                .filter(order -> customerOrders.stream()
                        .noneMatch(customerOrder -> customerOrder.getId().equals(order.getId())))
                .filter(order -> order.getStatus().equals(OrderStatus.PUBLISHED))
                .toList();
        orders.forEach(Order::defineAllTransactional);
        return orders;
    }

    @Override
    public Order createOrder(User user, OrderStatus orderStatus, Address deliveryAddress, Address departureAddress, String additionalInfo, String weight, String weightUnit, String dimensions, String dimensionsUnit, String cost, String currency, Date createdAt, Set<OrderCategory> orderCategories) {
        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(deliveryAddress);
        order.setDepartureAddress(departureAddress);
        order.setAdditionalInfo(additionalInfo);
        order.setWeight(weight);
        order.setWeightUnit(weightUnit);
        order.setDimensions(dimensions);
        order.setDimensionsUnit(dimensionsUnit);
        order.setCost(cost);
        order.setCurrency(currency);
        order.setCreationDate(createdAt);
        order.setCategories(orderCategories);
        return order;
    }
}
