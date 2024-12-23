package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.roadhaulage.entity.Estimate;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimate, Long> {
}
