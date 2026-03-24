package com.bancolombia.chocolatinazo.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Chocolatinazo application.
 * Implements Role-Based Access Control (RBAC) using Spring Security.
 *
 * Security Architecture:
 * - JWT tokens are used for authentication (stateless)
 * - Roles (PLAYER, AUDITOR, ADMIN) are used for authorization
 * - JwtRequestFilter intercepts all requests and validates tokens
 * - SessionCreationPolicy is STATELESS (no cookies/sessions)
 * - CSRF is disabled (not needed for JWT tokens)
 *
 * Endpoint Protection:
 * - Public (no auth required): /api/auth/register, /api/auth/login
 * - PLAYER role: /api/game/pick
 * - AUDITOR or ADMIN: /api/game/current, /api/audit/finished-games
 * - ADMIN only: /api/game/calculate-loser, /api/chocolatina/value
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configure HTTP security with RBAC for all endpoints.
     *
     * Configuration:
     * 1. CSRF disabled (not needed with JWT)
     * 2. Stateless session policy (JWT doesn't require sessions)
     * 3. Endpoint authorization by role
     * 4. JwtRequestFilter added to filter chain
     *
     * @param http HttpSecurity object to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using stateless JWT tokens
                .csrf(csrf -> csrf.disable())

                // Set stateless session policy - no server-side sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure endpoint authorization by role
                .authorizeHttpRequests(authz -> authz
                        // ========== PUBLIC ENDPOINTS (No authentication required) ==========
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // ========== PLAYER ROLE ENDPOINTS ==========
                        // Only players can pick a chocolate bar
                        .requestMatchers(HttpMethod.POST, "/api/game/pick").hasRole("PLAYER")

                        // ========== AUDITOR OR ADMIN ENDPOINTS ==========
                        // Auditors and admins can view current game records
                        .requestMatchers(HttpMethod.GET, "/api/game/current").hasAnyRole("AUDITOR", "ADMIN")
                        // Auditors and admins can view finished games
                        .requestMatchers(HttpMethod.GET, "/api/audit/finished-games").hasAnyRole("AUDITOR", "ADMIN")

                        // ========== ADMIN ONLY ENDPOINTS ==========
                        // Only admins can calculate loser and update the game state
                        .requestMatchers(HttpMethod.POST, "/api/game/calculate-loser").hasRole("ADMIN")
                        // Only admins can update the chocolate price
                        .requestMatchers(HttpMethod.PUT, "/api/chocolatina/value").hasRole("ADMIN")
                        // Only admins can manage user roles
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")

                        // ========== CATCH-ALL ==========
                        // All other requests require authentication (no specific role)
                        .anyRequest().authenticated()
                )

                // Add our JWT filter before the default UsernamePasswordAuthenticationFilter
                // This ensures tokens are  validated before any other authentication mechanisms
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Create AuthenticationManager bean for potential use in authentication processes.
     * Currently the application uses direct credential validation in AuthService,
     * but this bean is available for future extensions.
     *
     * @param http HttpSecurity
     * @return AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}

