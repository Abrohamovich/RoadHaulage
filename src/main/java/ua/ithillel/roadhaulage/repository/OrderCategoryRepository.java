package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.OrderCategory;

import java.util.Optional;

public interface OrderCategoryRepository extends JpaRepository<OrderCategory, Long> {
    Optional<OrderCategory> findByName(String name);
}
