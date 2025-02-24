package ua.ithillel.roadhaulage.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name"})
public class OrderCategoryDto {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
