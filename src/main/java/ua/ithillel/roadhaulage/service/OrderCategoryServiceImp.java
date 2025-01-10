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
    public void delete(OrderCategory orderCategory) {
        orderCategoryRepository.delete(orderCategory);
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
        Arrays.stream(categoryNames).forEach(name -> {
            Optional<OrderCategory> orderCategory = findByName(name);
            if (orderCategory.isEmpty()) {
                orderCategory = Optional.of(new OrderCategory());
                orderCategory.get().setName(name);
            }
            orderCategoriesSet.add(orderCategory.get());
        });
        return orderCategoriesSet;
    }


}
