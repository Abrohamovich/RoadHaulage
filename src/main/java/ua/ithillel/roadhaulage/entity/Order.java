package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OrderStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_order_category",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<OrderCategory> categories;
    @Transient
    private String categoriesString;

    @ManyToOne
    @JoinColumn(name = "departure_address_id")
    private Address departureAddress;
    @Transient
    private String departureAddressString;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;
    @Transient
    private String deliveryAddressString;

    private String weight;
    private String weightUnit;
    @Transient
    private String weightString;

    private String dimensions;
    private String dimensionsUnit;
    @Transient
    private String dimensionsString;

    private String cost;
    private String currency;
    @Transient
    private String costString;

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

    public void defineTransient(){
        this.categoriesString = this.getCategories().stream()
                .map(OrderCategory::getName)
                .collect(Collectors.joining(", "));
        this.departureAddressString = this.getDepartureAddress().toString();
        this.deliveryAddressString = this.getDeliveryAddress().toString();
        this.weightString = this.getWeight() + " " + this.getWeightUnit();
        this.dimensionsString = this.getDimensions() + " " + this.getDimensionsUnit();
        this.costString = this.getCost() + " " + this.getCurrency();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return Objects.equals(id, order.id);
    }
}


