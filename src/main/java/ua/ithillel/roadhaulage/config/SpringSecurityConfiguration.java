package ua.ithillel.roadhaulage.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.repository.UserRepository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfiguration {
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .usernameParameter("email")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/js/**",
                                "/css/**",
                                "/home",
                                "/logout",
                                "/register/**",
                                "/orders/**",
                                "/verify-email",
                                "/password-recovery/**").permitAll()
                        .requestMatchers("/account/**").hasAuthority(UserRole.USER.name())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
