package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import ua.ithillel.roadhaulage.dto.AddressDto;
import ua.ithillel.roadhaulage.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);
}
