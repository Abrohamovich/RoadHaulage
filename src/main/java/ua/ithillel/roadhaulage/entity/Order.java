package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "t_order")
@EqualsAndHashCode(of = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_order_category",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<OrderCategory> categories;

    @ManyToOne
    @JoinColumn(name = "departure_address_id")
    private Address departureAddress;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @Column(nullable = false)
    private String weight;
    @Column(nullable = false)
    private String weightUnit;

    @Column(nullable = false)
    private String dimensions;
    @Column(nullable = false)
    private String dimensionsUnit;

    @Column(nullable = false)
    private String cost;
    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String additionalInfo;

    private Date creationDate;
    private Date amendmentDate;
    private Date acceptDate;
    private Date completionDate;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;
}