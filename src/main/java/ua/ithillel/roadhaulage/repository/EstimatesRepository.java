package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ithillel.roadhaulage.entity.Estimate;

import java.util.List;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimate, Long> {
    List<Estimate> findEstimatesByCustomerId(long id);
    List<Estimate> findEstimatesByCourierId(long id);
}
