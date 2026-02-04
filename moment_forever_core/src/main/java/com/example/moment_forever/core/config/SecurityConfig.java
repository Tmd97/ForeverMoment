package com.example.moment_forever.core.config;

import com.example.moment_forever.core.security.JwtAuthenticationFilter;
import com.example.moment_forever.core.services.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig - Main security configuration
 *
 * Configures:
 * 1. Which endpoints are public/protected
 * 2. JWT authentication filter
 * 3. Password encoder
 * 4. UserDetailsService
 * 5. Session management (stateless)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,  // Enables @Secured annotation
        jsr250Enabled = true,   // Enables @RolesAllowed annotation
        prePostEnabled = true   // Enables @PreAuthorize, @PostAuthorize
)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Main security filter chain configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (we're using JWT, not sessions)
                .csrf(csrf -> csrf.disable())

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ENDPOINTS (no authentication required)
                        .requestMatchers("/api/auth/**").permitAll()           // Login, register
                        .requestMatchers("/api/public/**").permitAll()         // Public info
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // API docs
                        .requestMatchers("/error").permitAll()                 // Error pages

                        // ADMIN ENDPOINTS (requires ADMIN role)
                        .requestMatchers("/admin/**").hasRole("ADMIN")        // Admin portal
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")    // Admin API

                        // USER ENDPOINTS (requires USER role)
                        .requestMatchers("/api/user/**").hasRole("USER")      // User profile
                        .requestMatchers("/api/booking/**").hasRole("USER")   // Booking
                        .requestMatchers("/api/review/**").hasRole("USER")    // Reviews

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Configure session management (stateless = JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add JWT filter before username/password filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Authentication provider using our UserDetailsService and PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}