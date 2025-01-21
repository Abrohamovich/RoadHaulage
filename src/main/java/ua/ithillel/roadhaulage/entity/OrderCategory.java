package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "t_category")
public class OrderCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "categories")
    private List<Order> orders;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderCategory orderCategory = (OrderCategory) obj;
        return Objects.equals(id, orderCategory.id) &&  Objects.equals(name, orderCategory.name);
    }


}