package ua.ithillel.roadhaulage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.roadhaulage.entity.UserRole;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String phoneCode;
    private String email;
    private UserRole role;
    private boolean enabled;
    private String iban;
    private String password;
    private List<OrderDto> customerOrders;
    private List<OrderDto> courierOrders;
    private UserRatingDto rating;
}
