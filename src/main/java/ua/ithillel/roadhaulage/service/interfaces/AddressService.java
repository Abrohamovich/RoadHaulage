package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto save(AddressDto addressDto);
    List<AddressDto> findAll();
    AddressDto createAddress(String addressString);
}
