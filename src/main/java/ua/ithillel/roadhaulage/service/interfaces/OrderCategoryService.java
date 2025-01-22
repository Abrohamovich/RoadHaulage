package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.OrderCategory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderCategoryService {
    void save(OrderCategory orderCategory);
    Optional<OrderCategory> findByName(String name);
    List<OrderCategory> findAll();
    Set<OrderCategory> createOrderCategorySet(String categoryNames);
}
