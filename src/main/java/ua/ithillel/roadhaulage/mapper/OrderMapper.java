package ua.ithillel.roadhaulage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "categoriesString", ignore = true)
    @Mapping(target = "departureAddressString", ignore = true)
    @Mapping(target = "deliveryAddressString", ignore = true)
    OrderDto toDto(Order order);
    @Mapping(target = "categories.orders", ignore = true)
    @Mapping(target = "customer.customerOrders", ignore = true)
    @Mapping(target = "customer.courierOrders", ignore = true)
    @Mapping(target = "customer.rating", ignore = true)
    @Mapping(target = "courier.customerOrders", ignore = true)
    @Mapping(target = "courier.courierOrders", ignore = true)
    @Mapping(target = "courier.rating", ignore = true)
    Order toEntity(OrderDto orderDto);
}
