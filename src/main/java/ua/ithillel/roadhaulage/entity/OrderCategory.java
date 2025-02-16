package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "t_category")
@EqualsAndHashCode(of = {"id", "name"})
public class OrderCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "categories")
    private List<Order> orders;
}