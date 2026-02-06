package com.example.stock.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize annotations
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST API (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless session - no server-side session storage
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // RBAC Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                        // ═══════════════════════════════════════════════════════════
                        // PUBLIC ENDPOINTS - No authentication required
                        // ═══════════════════════════════════════════════════════════


                        // ═══════════════════════════════════════════════════════════
                        // DEFAULT - Require authentication for any other request
                        // ═══════════════════════════════════════════════════════════


                // Use custom UserDetailsService for authentication
                .userDetailsService(userDetailsService)

                // HTTP Basic Authentication for API testing
                .httpBasic(basic -> {
                });

        return http.build();
    }

    /**
     * Password encoder using BCrypt algorithm
     * BCrypt automatically handles salting and is resistant to brute-force attacks
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}