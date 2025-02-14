package ua.ithillel.roadhaulage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.roadhaulage.entity.Order;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCategoryDto {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
