package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.AddressDto;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressDto save(AddressDto addressDto);
    List<AddressDto> findAll(int page, int pageSize);
    Optional<AddressDto> findById(long id);
    AddressDto createAddress(String addressString);
}
