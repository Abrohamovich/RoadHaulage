package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.repository.OrderCategoryRepository;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderCategoryServiceImp implements OrderCategoryService {
    private OrderCategoryRepository orderCategoryRepository;

    @Override
    public void save(OrderCategory orderCategory) {
        orderCategoryRepository.save(orderCategory);
    }

    @Override
    public Optional<OrderCategory> findByName(String name) {
        return orderCategoryRepository.findByName(name);
    }

    @Override
    public List<OrderCategory> findAll() {
        return orderCategoryRepository.findAll();
    }

    @Override
    public Set<OrderCategory> createOrderCategorySet(String categoryNamesString) {
        String[] categoryNames = Arrays.stream(categoryNamesString.split(","))
                .map(String::trim)
                .map(category -> Arrays.stream(category.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);

        Set<OrderCategory> orderCategoriesSet = new HashSet<>();
        for(String name : categoryNames) {
            Optional<OrderCategory> orderCategory = orderCategoryRepository.findByName(name);
            if(orderCategory.isEmpty()) {
                OrderCategory category = new OrderCategory();
                category.setName(name);
                orderCategory = Optional.of(category);
            }
            orderCategoriesSet.add(orderCategory.get());
        }
        return orderCategoriesSet;
    }


}
