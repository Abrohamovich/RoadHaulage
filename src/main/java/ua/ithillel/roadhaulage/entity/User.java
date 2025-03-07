package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_user")
@EqualsAndHashCode(of = {"id", "email", "countryCode", "localPhone"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String countryCode;
    @Column(nullable = false)
    private String localPhone;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(unique = true)
    private String iban;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> customerOrders; // List of orders you have created
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> courierOrders; // List of orders you have fulfilled for other users
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserRating rating;
}