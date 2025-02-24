package ua.ithillel.roadhaulage.dto;

import lombok.*;
import ua.ithillel.roadhaulage.entity.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
