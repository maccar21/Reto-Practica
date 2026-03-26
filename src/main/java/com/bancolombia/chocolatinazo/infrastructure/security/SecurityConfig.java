package com.bancolombia.chocolatinazo.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Chocolatinazo application.
 * Implements Role-Based Access Control (RBAC) using Spring Security.
 *
 * <p>Security Architecture:</p>
 * <ul>
 *   <li>JWT tokens are used for authentication (stateless)</li>
 *   <li>Roles (PLAYER, AUDITOR, ADMIN) are used for authorization</li>
 *   <li>JwtRequestFilter intercepts all requests and validates tokens</li>
 *   <li>SessionCreationPolicy is STATELESS (no cookies/sessions)</li>
 *   <li>CSRF is disabled (not needed for JWT tokens)</li>
 * </ul>
 *
 * <p>Endpoint Protection:</p>
 * <ul>
 *   <li>Public (no auth required): /api/auth/register, /api/auth/login</li>
 *   <li>PLAYER role: /api/game/pick</li>
 *   <li>AUDITOR or ADMIN: /api/audit/** endpoints</li>
 *   <li>ADMIN only: /api/game/calculate-loser, /api/chocolatina/value, /api/users/**</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROLE_ADMIN   = "ADMIN";
    private static final String ROLE_AUDITOR = "AUDITOR";
    private static final String ROLE_PLAYER  = "PLAYER";

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configure HTTP security with RBAC for all endpoints.
     *
     * <p>Configuration:</p>
     * <ol>
     *   <li>CSRF disabled (not needed with JWT)</li>
     *   <li>Stateless session policy (JWT doesn't require sessions)</li>
     *   <li>Endpoint authorization by role</li>
     *   <li>JwtRequestFilter added to filter chain</li>
     * </ol>
     *
     * @param http HttpSecurity object to configure
     * @return SecurityFilterChain
     * @throws org.springframework.security.config.annotation.AlreadyBuiltException if already built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws org.springframework.security.config.annotation.AlreadyBuiltException {
        try {
            http
                    // Disable CSRF since we're using stateless JWT tokens
                    .csrf(AbstractHttpConfigurer::disable)

                    // Set stateless session policy - no server-side sessions
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // Configure endpoint authorization by role
                    .authorizeHttpRequests(authz -> authz
                            // ========== PUBLIC ENDPOINTS (No authentication required) ==========
                            .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                            // ========== PLAYER ROLE ENDPOINTS ==========
                            .requestMatchers(HttpMethod.POST, "/api/game/pick").hasRole(ROLE_PLAYER)

                            // ========== AUDITOR OR ADMIN ENDPOINTS ==========
                            .requestMatchers(HttpMethod.GET, "/api/audit/current-game").hasAnyRole(ROLE_AUDITOR, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/audit/current-game/records").hasAnyRole(ROLE_AUDITOR, ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/audit/finished-games").hasAnyRole(ROLE_AUDITOR, ROLE_ADMIN)

                            // ========== ADMIN ONLY ENDPOINTS ==========
                            .requestMatchers(HttpMethod.POST, "/api/game/create").hasRole(ROLE_ADMIN)
                            .requestMatchers(HttpMethod.POST, "/api/game/calculate-loser").hasRole(ROLE_ADMIN)
                            .requestMatchers(HttpMethod.PUT, "/api/chocolatina/value").hasRole(ROLE_ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/chocolatina/value").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole(ROLE_ADMIN)

                            // ========== CATCH-ALL ==========
                            .anyRequest().authenticated()
                    )

                    // Add our JWT filter before the default UsernamePasswordAuthenticationFilter
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new org.springframework.security.config.annotation.AlreadyBuiltException("Error configuring security filter chain: " + e.getMessage());
        }
    }

    /**
     * Create AuthenticationManager bean for potential use in authentication processes.
     * Currently, the application uses direct credential validation in AuthService,
     * but this bean is available for future extensions.
     *
     * @param http HttpSecurity
     * @return AuthenticationManager
     * @throws org.springframework.security.config.annotation.AlreadyBuiltException if already built
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws org.springframework.security.config.annotation.AlreadyBuiltException {
        try {
            return http.getSharedObject(AuthenticationManagerBuilder.class).build();
        } catch (Exception e) {
            throw new org.springframework.security.config.annotation.AlreadyBuiltException("Error building AuthenticationManager: " + e.getMessage());
        }
    }
}

