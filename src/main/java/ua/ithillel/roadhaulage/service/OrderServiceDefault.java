package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.mapper.OrderMapper;
import ua.ithillel.roadhaulage.repository.OrderRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceDefault implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto save(OrderDto orderDto) {
        Order savedOrder = orderRepository.save(orderMapper.toEntity(orderDto));
        return orderMapper.toDto(savedOrder);
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
    public List<OrderDto> findAllPageable(int page, int pageSize) {
        return orderRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(orderMapper::toDto)
                .toList();
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
