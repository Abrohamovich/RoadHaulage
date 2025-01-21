package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.repository.AddressRepository;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImp implements AddressService {
    private AddressRepository addressRepository;

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> createAddress(String addressString) {
        String[] addressFields = Arrays.stream(addressString.split(","))
                .map(String::trim)
                .map(category -> Arrays.stream(category.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);
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
            address = Optional.of(newAddress);
        }
        return address;
    }
}
