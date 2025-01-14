package ua.ithillel.roadhaulage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAll();
    Optional<Address> findByStreetAndCityAndStateAndZipAndCountry(String street, String city, String state, String zip, String country);
}
