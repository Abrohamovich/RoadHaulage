package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Table(name="t_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String phone;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(unique = true)
    private String iban;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> customerOrders; // List of orders you have created
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL)
    private List<Order> courierOrders; // List of orders you have fulfilled for other users

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone);
    }

    @Override
    public String toString() {
        return "User{" +
                "iban='" + iban + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
