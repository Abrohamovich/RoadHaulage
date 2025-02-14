package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.mapper.AddressMapper;
import ua.ithillel.roadhaulage.mapper.OrderMapper;
import ua.ithillel.roadhaulage.repository.OrderRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceDefault implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public void save(OrderDto orderDto) {
        orderRepository.save(orderMapper.toEntity(orderDto));
    }

    @Override
    public void delete(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> findOrdersByCustomerId(long id) {
        return orderRepository.findOrdersByCustomerId(id)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDto> findOrdersByCourierId(long id) {
        return orderRepository.findOrdersByCourierId(id)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public void update(OrderDto orderDto) {
        Optional<Order> orderDB = orderRepository.findById(orderDto.getId());
        if (orderDB.isPresent()) {
            orderRepository.save(orderMapper.toEntity(orderDto));
        }
    }

    @Override
    public Optional<OrderDto> findById(long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderDto> returnOtherPublishedOrders(long id) {
        List<Order> allOrders = orderRepository.findAll();
        List<Order> customerOrders = orderRepository.findOrdersByCustomerId(id);
        List<Order> orders = allOrders.stream()
                .filter(order -> customerOrders.stream()
                        .noneMatch(customerOrder -> customerOrder.getId().equals(order.getId())))
                .filter(order -> order.getStatus().equals(OrderStatus.PUBLISHED))
                .toList();
        return orders.stream().map(orderMapper::toDto).toList();
    }
}
