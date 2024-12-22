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
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private String role; // BUYER or HAULIER
    @Column(unique = true)
    private String iban;
    private String password;
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estimate> buyerEstimates; //always empty if role is HAULIER
    @OneToMany(mappedBy = "haulier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estimate> haulierEstimates; //always empty if role is BUYER

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return List.of(grantedAuthority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
