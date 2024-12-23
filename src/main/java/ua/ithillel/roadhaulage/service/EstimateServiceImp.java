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
//        estimate.setBuyerFullName(estimate.getBuyerFullName());
//        estimate.setDeliveryAddress(estimate.getDeliveryAddress());
//        estimate.setDepartureAddress(estimate.getDepartureAddress());
//        estimate.setAdditionalInfo(estimate.getAdditionalInfo());
//        estimate.setWeight(estimate.getWeight());
//        estimate.setDimensions(estimate.getDimensions());
//        estimate.setCost(estimate.getCost());
//        estimate.setAcceptDate(estimate.getAcceptDate());
        estimatesRepository.save(estimate);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Estimate> findAll() {
        return List.of();
    }
}
