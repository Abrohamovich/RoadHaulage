package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.mapper.OrderMapper;
import ua.ithillel.roadhaulage.repository.OrderRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceDefault implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto save(OrderDto orderDto) {
        Order savedOrder = orderRepository.save(orderMapper.toEntity(orderDto));
        log.info("Saved order: {}", orderMapper.toDto(savedOrder));
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting order with id: {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> findOrdersByCustomerId(long id) {
        log.info("Finding orders by customer id: {}", id);
        return orderRepository.findOrdersByCustomerId(id)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public Page<OrderDto> findPageableOrdersByCustomerId(long id, int page, int size) {
        return orderRepository.findOrdersByCustomerId(id, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderDto> findOrdersByCourierId(long id) {
        log.info("Finding orders by courier id: {}", id);
        return orderRepository.findOrdersByCourierId(id)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public Page<OrderDto> findPageableOrdersByCourierId(long id, int page, int size) {
        return orderRepository.findOrdersByCourierId(id, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderDto> findAllPageable(int page, int pageSize) {
        log.info("Finding all orders pageable, page: {}, pageSize: {}", page, pageSize);
        return orderRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public Optional<OrderDto> findById(long id) {
        log.info("Finding order by id: {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderDto> returnOtherPublishedOrders(long id) {
        log.info("Returning other published orders by customer id: {}", id);
        List<Order> allOrders = orderRepository.findAll();
        List<Order> customerOrders = orderRepository.findOrdersByCustomerId(id);
        List<Order> orders = allOrders.stream()
                .filter(order -> customerOrders.stream()
                        .noneMatch(customerOrder -> customerOrder.getId().equals(order.getId())))
                .filter(order -> order.getStatus().equals(OrderStatus.PUBLISHED))
                .toList();
        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public Page<OrderDto> returnOtherPublishedPageableOrders(long id, int page, int size) {
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, size));

        List<OrderDto> filteredOrders = orderPage.getContent().stream()
                .filter(o -> !o.getCustomer().getId().equals(id))
                .filter(o -> o.getStatus() == OrderStatus.PUBLISHED)
                .map(orderMapper::toDto)
                .toList(); // Collect into a List

        return new PageImpl<>(filteredOrders, PageRequest.of(page, size), orderPage.getTotalElements());
    }
}
