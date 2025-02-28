package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public List<OrderDto> findOrdersByCourierId(long id) {
        log.info("Finding orders by courier id: {}", id);
        return orderRepository.findOrdersByCourierId(id)
                .stream()
                .map(orderMapper::toDto)
                .toList();
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
    public Page<OrderDto> findOrdersByCourierIdAndStatus(long id, OrderStatus status, int page, int size) {
        log.info("Finding orders for courier ID: {}, status: {}, page: {}, size: {}", id, status, page, size);
        return orderRepository.findOrdersByCourierIdAndStatus(id, status, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDto> findOrdersByCustomerIdAndStatus(long id, OrderStatus status, int page, int size) {
        log.info("Finding orders for customer ID: {}, status: {}, page: {}, size: {}", id, status, page, size);
        return orderRepository.findOrdersByCustomerIdAndStatus(id, status, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDto> findOrdersByCustomerIdAndStatusNot(long id, OrderStatus status, int page, int size) {
        log.info("Finding orders for courier ID: {}, status not: {}, page: {}, size: {}", id, status, page, size);
        return orderRepository.findOrdersByCustomerIdAndStatusNot(id, status, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDto> findOrdersByCustomerIdNotAndStatus(long id, OrderStatus status, int page, int size) {
        log.info("Finding orders for courier ID not: {}, status: {}, page: {}, size: {}", id, status, page, size);
        return orderRepository.findOrdersByCustomerIdNotAndStatus(id, status, PageRequest.of(page, size))
                .map(orderMapper::toDto);
    }

}
