package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="t_user_rating")
public class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    private double average;
    private long count;
}
