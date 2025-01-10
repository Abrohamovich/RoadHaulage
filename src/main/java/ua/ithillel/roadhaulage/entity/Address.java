package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street; // Lietzenburger Straße 6
    private String city; // Gummersbach
    private String state; // Nordhein-Westfallen
    private String zip; // 51645
    private String country; // Germany
}
