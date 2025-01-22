package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.repository.AddressRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AddressServiceImpTests {
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressServiceImp addressService;

    @Test
    public void saveTest(){
        Address address = mock(Address.class);

        addressService.save(address);

        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void findAllTest_returnsFullList(){
        List<Address> mockAddresses = Arrays.asList(mock(Address.class), mock(Address.class));
        when(addressRepository.findAll()).thenReturn(mockAddresses);

        List<Address> result = addressService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void findAllTest_returnsEmptyList(){
        when(addressRepository.findAll()).thenReturn(List.of());

        List<Address> result = addressService.findAll();

        assertEquals(0, result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void createAddressTest_returnsNewAddress(){
        String addressString = "42 Pastera street, Odesa, Odesa oblast, 6500, Ukraine";
        String street = "42 Pastera Street";
        String city = "Odesa";
        String state = "Odesa Oblast";
        String zip = "6500";
        String country = "Ukraine";
        when(addressRepository.findByStreetAndCityAndStateAndZipAndCountry(
                street, city, state, zip, country
        )).thenReturn(Optional.empty());

        Optional<Address> result = addressService.createAddress(addressString);

        assertTrue(result.isPresent());
    }

    @Test
    public void createAddressTest_returnsExistedAddress(){
        String addressString = "42 Pastera street, Odesa, Odesa oblast, 6500, Ukraine";
        String street = "42 Pastera Street";
        String city = "Odesa";
        String state = "Odesa Oblast";
        String zip = "6500";
        String country = "Ukraine";

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZip(zip);
        address.setCountry(country);

        when(addressRepository.findByStreetAndCityAndStateAndZipAndCountry(
                street, city, state, zip, country
        )).thenReturn(Optional.of(address));

        Optional<Address> result = addressService.createAddress(addressString);

        assertTrue(result.isPresent());
        assertEquals(street, result.get().getStreet());
        assertEquals(city, result.get().getCity());
        assertEquals(state, result.get().getState());
        assertEquals(zip, result.get().getZip());
        assertEquals(country, result.get().getCountry());
    }
}
