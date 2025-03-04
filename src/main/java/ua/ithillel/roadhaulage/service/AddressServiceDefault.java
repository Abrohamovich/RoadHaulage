package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.mapper.AddressMapper;
import ua.ithillel.roadhaulage.repository.AddressRepository;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceDefault implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDto save(AddressDto addressDto) {
        Address savedAddress = addressRepository.save(addressMapper.toEntity(addressDto));
        log.info("Saving address: {}", savedAddress);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    public List<AddressDto> findAll(int page, int pageSize) {
        log.info("Returning all addresses");
        return addressRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public Optional<AddressDto> findById(long id) {
        log.info("Finding address by id: {}", id);
        return addressRepository.findById(id)
                .map(addressMapper::toDto);
    }

    @Override
    public AddressDto createAddress(String addressString) {
        log.info("Creating addressDto from string: {}", addressString);
        String[] addressFields = Arrays.stream(addressString.split(","))
                .map(String::trim)
                .map(str -> Arrays.stream(str.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase()
                                + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);
        if (addressFields.length != 5) {
            log.warn("Invalid address as string: {}", addressString);
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
            log.info("Created new address : {}", newAddress);
            return addressMapper.toDto(newAddress);
        } else {
            log.info("Address already exists: {}", address.get());
            return addressMapper.toDto(address.get());
        }
    }
}
