package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.VerificationToken;

@Mapper(componentModel = "spring")
public interface VerificationTokenMapper {
    VerificationTokenDto toDto(VerificationToken verificationToken);
    VerificationToken toEntity(VerificationTokenDto verificationTokenDto);
}
