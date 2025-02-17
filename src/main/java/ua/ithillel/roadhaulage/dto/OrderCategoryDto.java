package ua.ithillel.roadhaulage.dto;

import lombok.*;
import ua.ithillel.roadhaulage.entity.Order;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class OrderCategoryDto {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
