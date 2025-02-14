package ua.ithillel.roadhaulage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + ", " + zip + ", " + country;
    }
}
