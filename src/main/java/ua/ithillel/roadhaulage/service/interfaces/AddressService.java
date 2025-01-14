package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AddressService {
    void save(Address address);
    void delete(Address address);
    List<Address> findAll();
    Optional<Address> createAddress(String addressString);
}
