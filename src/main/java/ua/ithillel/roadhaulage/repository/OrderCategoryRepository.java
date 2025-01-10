package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderCategoryRepository extends JpaRepository<OrderCategory, Long> {
    Optional<OrderCategory> findByName(String name);
    List<OrderCategory> findAll();
}
