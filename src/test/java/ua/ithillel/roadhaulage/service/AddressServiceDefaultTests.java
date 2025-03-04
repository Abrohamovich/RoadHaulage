package ua.ithillel.roadhaulage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.mapper.AddressMapper;
import ua.ithillel.roadhaulage.repository.AddressRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceDefaultTests {
    @InjectMocks
    private AddressServiceDefault addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    private AddressDto addressDto;
    private Address address;

    @BeforeEach
    void init() {
        addressDto = new AddressDto();
        addressDto.setId(1L);
        addressDto.setStreet("Street");
        addressDto.setCity("City");
        addressDto.setState("State");
        addressDto.setZip("68932");
        addressDto.setCountry("Country");
        address = new Address();
        address.setId(1L);
        address.setStreet("Street");
        address.setCity("City");
        address.setState("State");
        address.setZip("68932");
        address.setCountry("Country");
    }

    @Test
    void save() {
        when(addressMapper.toEntity(addressDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        AddressDto result = addressService.save(addressDto);

        assertNotNull(result);
        assertEquals(addressDto, result);
    }

    @Test
    void findAll_returnsFullList() {
        Page<Address> addressPage = new PageImpl<>(List.of(address));

        when(addressRepository.findAll(any(PageRequest.class))).thenReturn(addressPage);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        List<AddressDto> result = addressService.findAll(1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressDto, result.getFirst());
    }

    @Test
    void findAll_returnsEmptyList() {
        when(addressRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        List<AddressDto> result = addressService.findAll(1, 1);

        assertEquals(0, result.size());
    }

    @Test
    void findById_returnsPresentOptional() {
        when(addressRepository.findById(anyLong())).
                thenReturn(Optional.of(address));
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        Optional<AddressDto> result = addressService.findById(1);

        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    @Test
    void findById_returnsEmptyOptional() {
        when(addressRepository.findById(anyLong())).
                thenReturn(Optional.empty());

        Optional<AddressDto> result = addressService.findById(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createAddress_returnsNewAddress() {
        String addressString = "Street, City, State, 68932, Country";
        String street = "Street";
        String city = "City";
        String state = "State";
        String zip = "68932";
        String country = "Country";

        when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

        when(addressRepository.findByStreetAndCityAndStateAndZipAndCountry(
                street, city, state, zip, country
        )).thenReturn(Optional.empty());

        AddressDto result = addressService.createAddress(addressString);

        assertNotNull(result);
        assertEquals(street, result.getStreet());
        assertEquals(city, result.getCity());
        assertEquals(state, result.getState());
        assertEquals(zip, result.getZip());
        assertEquals(country, result.getCountry());
    }

    @Test
    void createAddress_returnsExistedAddress() {
        String addressString = "Street, City, State, 68932, Country";
        String street = "Street";
        String city = "City";
        String state = "State";
        String zip = "68932";
        String country = "Country";

        when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

        when(addressRepository.findByStreetAndCityAndStateAndZipAndCountry(
                street, city, state, zip, country
        )).thenReturn(Optional.of(address));

        AddressDto result = addressService.createAddress(addressString);

        assertEquals(street, result.getStreet());
        assertEquals(city, result.getCity());
        assertEquals(state, result.getState());
        assertEquals(zip, result.getZip());
        assertEquals(country, result.getCountry());
    }

    @Test
    void createAddress_throwsAddressCreateException() {
        String addressString = "42 Pastera street";

        AddressCreateException exception = assertThrows(AddressCreateException.class, () ->
                addressService.createAddress(addressString));

        assertTrue(exception.getMessage()
                .contains("You didn't enter the address information correctly"));
    }
}