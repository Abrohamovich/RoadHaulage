package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.mapper.AddressMapper;
import ua.ithillel.roadhaulage.repository.AddressRepository;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AddressServiceDefault implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDto save(AddressDto addressDto) {
        Address savedAddress = addressRepository.save(addressMapper.toEntity(addressDto));
        return addressMapper.toDto(savedAddress);
    }

    @Override
    public List<AddressDto> findAll(int page, int pageSize) {
        return addressRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public Optional<AddressDto> findById(long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto);
    }

    @Override
    public AddressDto createAddress(String addressString) {
        String[] addressFields = Arrays.stream(addressString.split(","))
                .map(String::trim)
                .map(str -> Arrays.stream(str.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase()
                                + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);
        if(addressFields.length != 5) {
            throw new AddressCreateException("You didn't enter the address information correctly");
        }
        Optional<Address> address = addressRepository.findByStreetAndCityAndStateAndZipAndCountry(
                addressFields[0], addressFields[1], addressFields[2], addressFields[3], addressFields[4]
        );
        if (address.isEmpty()) {
            Address newAddress = new Address();
            newAddress.setStreet(addressFields[0]);
            newAddress.setCity(addressFields[1]);
            newAddress.setState(addressFields[2]);
            newAddress.setZip(addressFields[3]);
            newAddress.setCountry(addressFields[4]);
            return addressMapper.toDto(newAddress);
        } else {
            return addressMapper.toDto(address.get());
        }
    }
}
