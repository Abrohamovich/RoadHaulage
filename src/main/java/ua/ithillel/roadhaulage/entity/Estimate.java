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
    private String buyerFullName;
    private String haulierFullName;
    private String deliveryAddress;
    private String departureAddress;
    private String additionalInfo;
    private String weight; // in metric sys (kg, t ...)
    private String dimensions; // LENGTH-WIDTH-HIGH in metric sys (m, sm)
    private String cost; // example: 700 USD or 8,340 UAH or 560 BYN
    private Date acceptDate; // YYYY-MM-DD 2024-12-27
    private Date executeDate; // YYYY-MM-DD 2025-01-09
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
    @ManyToOne
    @JoinColumn(name = "haulier_id")
    private User haulier;
}
