package ua.ithillel.roadhaulage.dto;

import lombok.*;
import ua.ithillel.roadhaulage.entity.OrderStatus;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private Set<OrderCategoryDto> categories;
    private String categoriesString;
    private AddressDto departureAddress;
    private String departureAddressString;
    private AddressDto deliveryAddress;
    private String deliveryAddressString;
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

    public void defineView() {
        this.categoriesString = this.categories.stream()
                .map(OrderCategoryDto::toString)
                .collect(Collectors.joining(", "));
        this.departureAddressString = this.departureAddress.toString();
        this.deliveryAddressString = this.deliveryAddress.toString();
    }
}
