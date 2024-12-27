package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name="t_estimate")
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category; // goods, food, toys...
    private String shippingAddress;
    private String deliveryAddress;
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
}
