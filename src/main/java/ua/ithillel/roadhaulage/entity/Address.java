package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "t_address")
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String street; // Lietzenburger Straße 6
    @Column(nullable = false)
    private String city; // Gummersbach
    @Column(nullable = false)
    private String state; // Nordhein-Westfallen
    @Column(nullable = false)
    private String zip; // 51645
    @Column(nullable = false)
    private String country; // Germany
}