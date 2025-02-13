package ua.ithillel.roadhaulage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.roadhaulage.entity.OrderStatus;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private Set<OrderCategoryDto> categories;
    private AddressDto departureAddress;
    private AddressDto deliveryAddress;
    private String weight;
    private String weightUnit;
    private String dimensions;
    private String dimensionsUnit;
    private String cost;
    private String currency;
    private String additionalInfo;
    private Date creationDate;
    private Date amendmentDate;
    private Date acceptDate;
    private Date completionDate;
    private UserDto customer;
    private UserDto courier;
}
