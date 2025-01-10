package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.Address;

import java.util.List;

public interface AddressService {
    void save(Address address);
    void delete(Address address);
    List<Address> findAll();
    Address createAddress(String addressString);
}
