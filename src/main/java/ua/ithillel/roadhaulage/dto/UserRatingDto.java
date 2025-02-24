package ua.ithillel.roadhaulage.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRatingDto {
    private Long id;
    private UserDto user;
    private double average;
    private long count;
}
