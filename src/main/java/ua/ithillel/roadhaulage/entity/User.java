package ua.ithillel.roadhaulage.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name="t_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private boolean enabled;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String phone;
    private String role; // USER, ADMIN
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
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return List.of(grantedAuthority);
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


}
