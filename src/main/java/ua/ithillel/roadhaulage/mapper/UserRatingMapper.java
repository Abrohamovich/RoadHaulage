package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.UserRating;

@Mapper(componentModel = "spring")
public interface UserRatingMapper {
    UserRatingDto toDto(UserRating userRating);
    @Mapping(target = "user.customerOrders", ignore = true)
    @Mapping(target = "user.courierOrders", ignore = true)
    @Mapping(target = "user.rating", ignore = true)
    UserRating toEntity(UserRatingDto userRatingDto);
}
