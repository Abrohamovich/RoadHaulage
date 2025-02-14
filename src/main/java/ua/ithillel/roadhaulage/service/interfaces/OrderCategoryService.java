package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.OrderCategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderCategoryService {
    OrderCategoryDto save(OrderCategoryDto orderCategoryDto);
    Optional<OrderCategoryDto> findByName(String name);
    List<OrderCategoryDto> findAll();
    Set<OrderCategoryDto> createOrderCategorySet(String categoryNames);
}
