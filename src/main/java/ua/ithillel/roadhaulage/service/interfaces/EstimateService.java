package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.Estimate;

import java.util.List;

public interface EstimateService {
    boolean save(Estimate estimate);
    boolean delete(Long id);
    List<Estimate> findEstimatesByCustomerId(long id);
    List<Estimate> findEstimatesByCourierId(long id);
    List<Estimate> findAll();
}
