package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
}
