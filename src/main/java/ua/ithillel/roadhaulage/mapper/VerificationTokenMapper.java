package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.VerificationToken;

@Mapper(componentModel = "spring")
public interface VerificationTokenMapper {
    VerificationTokenDto toDto(VerificationToken verificationToken);
    @Mapping(target = "user.customerOrders", ignore = true)
    @Mapping(target = "user.courierOrders", ignore = true)
    @Mapping(target = "user.rating", ignore = true)
    VerificationToken toEntity(VerificationTokenDto verificationTokenDto);
}
