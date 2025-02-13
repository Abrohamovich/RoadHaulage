package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.UserRating;

@Mapper(componentModel = "spring")
public interface UserRatingMapper {
    UserRatingDto toDto(UserRating userRating);
    UserRating toEntity(UserRatingDto userRatingDto);
}
