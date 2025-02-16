package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.entity.OrderCategory;

@Mapper(componentModel = "spring")
public interface OrderCategoryMapper {
    OrderCategoryDto toDto(OrderCategory orderCategory);
    @Mapping(target = "orders", ignore = true)
    OrderCategory toEntity(OrderCategoryDto orderCategoryDto);
}
