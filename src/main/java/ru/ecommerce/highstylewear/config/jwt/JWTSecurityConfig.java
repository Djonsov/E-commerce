package ru.ecommerce.highstylewear.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.ecommerce.highstylewear.constants.Errors;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetailsService;

import static ru.ecommerce.highstylewear.constants.SecurityConstants.*;
import static ru.ecommerce.highstylewear.constants.UserRolesConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile({"dev", "prod"})
public class JWTSecurityConfig {
    private final JWTTokenFilter jwtTokenFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTSecurityConfig(JWTTokenFilter jwtTokenFilter,
                             CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                //Настройка http-запросов - кому/куда можно/нельзя
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(USERS_REST_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(ITEMS_REST_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(BUCKETS_REST_PERMISSION_LIST.toArray(String[]::new)).hasRole(USER)
                        .requestMatchers(BUCKETS_REST_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers("/api/rest/buckets/**").permitAll()
                        .requestMatchers("/api/rest/orders/order").hasRole(USER)
                        .requestMatchers("/api/rest/orders/placeOrder").permitAll()
                        .requestMatchers("/api/rest/users/update/**").hasAnyRole(ADMIN,STUFF)
                        .requestMatchers(ITEMS_REST_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, STUFF)
                        .requestMatchers(ORDERS_REST_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, STUFF)
                        .requestMatchers(USERS_REST_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, STUFF)
                        .anyRequest().authenticated()
                )
                .exceptionHandling()
                //.authenticationEntryPoint()
                .and()
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //JWT Token Filter VALID OR NOT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(customUserDetailsService)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
