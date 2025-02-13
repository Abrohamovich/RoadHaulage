package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.entity.OrderCategory;

@Mapper(componentModel = "spring")
public interface OrderCategoryMapper {
    OrderCategoryDto toDto(OrderCategory orderCategory);
    OrderCategory toEntity(OrderCategoryDto orderCategoryDto);
}
