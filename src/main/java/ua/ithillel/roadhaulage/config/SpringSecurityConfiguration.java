package ua.ithillel.roadhaulage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.filter.JwtAuthenticationFilter;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                        .usernameParameter("email")
//                        .defaultSuccessUrl("/home", true)
//                        .failureUrl("/login?error=true")
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/js/**",
//                                "/css/**",
//                                "/home",
//                                "/about-us",
//                                "/contact",
//                                "/logout",
//                                "/register/**",
//                                "/orders/**",
//                                "/verify-email",
//                                "/password-recovery/**").permitAll()
//                        .requestMatchers("/account/**", "/generate-report").hasAuthority(UserRole.USER.name())
//                        .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.name())
//                        .anyRequest().authenticated()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/home")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll())
//                .build();
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/js/**",
                                "/css/**",
                                "/home",
                                "/about-us",
                                "/contact",
                                "/logout",
                                "/register/**",
                                "/orders/**",
                                "/verify-email",
                                "/password-recovery/**",
                                "/api/auth/**",
                                "login",
                                "error").permitAll()
                        .requestMatchers("/account/**", "/generate-report").hasAuthority(UserRole.USER.name())
                        .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userService);
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
