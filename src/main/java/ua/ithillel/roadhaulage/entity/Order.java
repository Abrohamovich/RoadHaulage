package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "t_order_category",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<OrderCategory> categories;
    @Transient
    private String categoryNames;
    @ManyToOne
    @JoinColumn(name = "departure_address_id")
    private Address departureAddress; // From
    @Transient
    private String departureAddressString;
    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress; // To
    @Transient
    private String deliveryAddressString;
    private String additionalInfo;
    private String weight; // in metric sys (kg, t ...)
    private String status; // CREATED, PUBLISHED, IN-PROCESS, COMPLETED
    private String dimensions; // LENGTH-WIDTH-HIGH in metric sys (m, sm)
    private String cost; // example: 700 USD or 8,340 UAH or 560 BYN
    private Date creationDate; // YYYY-MM-DD 2024-12-27
    private Date amendmentDate; // YYYY-MM-DD 2025-01-09
    private Date acceptDate; // YYYY-MM-DD 2025-01-09
    private Date completionDate; // YYYY-MM-DD 2025-01-09
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;


    public void defineCategoryNames(){
        this.categoryNames = this.getCategories().stream()
                .map(OrderCategory::getName)
                .collect(Collectors.joining(", "));
    }

}
