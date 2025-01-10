package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.repository.AddressRepository;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImp implements AddressService {
    private AddressRepository addressRepository;

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(String addressString) {
        String[] addressFields = Arrays.stream(addressString.split(","))
                .map(String::trim)
                .map(category -> Arrays.stream(category.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .reduce((a, b) -> a + " " + b).orElse(""))
                .toArray(String[]::new);
        Address address = new Address();
        address.setStreet(addressFields[0]);
        address.setCity(addressFields[1]);
        address.setState(addressFields[2]);
        address.setZip(addressFields[3]);
        address.setCountry(addressFields[4]);
        return address;
    }
}
