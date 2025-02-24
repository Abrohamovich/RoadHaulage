package ua.ithillel.roadhaulage.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    @Override
    public String toString() {
        return street + ", " +
                city + ", " +
                state + ", " +
                zip + ", " + country;
    }
}
