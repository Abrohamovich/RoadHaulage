package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.Estimate;
import ua.ithillel.roadhaulage.repository.EstimatesRepository;
import ua.ithillel.roadhaulage.service.interfaces.EstimateService;

import java.util.List;

@Service
@AllArgsConstructor
public class EstimateServiceImp implements EstimateService {
    private EstimatesRepository estimatesRepository;

    @Override
    public boolean save(Estimate estimate) {
        estimatesRepository.save(estimate);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Estimate> findEstimatesByCustomerId(long id) {
        return estimatesRepository.findEstimatesByCustomerId(id);
    }

    @Override
    public List<Estimate> findEstimatesByCourierId(long id) {
        return estimatesRepository.findEstimatesByCourierId(id);
    }

    @Override
    public List<Estimate> findAll() {
        return List.of();
    }
}
