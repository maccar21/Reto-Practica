package com.bancolombia.chocolatinazo.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT Request Filter that intercepts every request (except public endpoints).
 * Extracts the Bearer token from the Authorization header, validates it,
 * and sets the SecurityContext with the authenticated user information.
 *
 * This filter is executed once per request and adds the ROLE_ prefix to the role
 * as required by Spring Security's hasRole() and hasAnyRole() methods.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Intercepts the request, extracts JWT token, validates it, and sets the SecurityContext.
     * If token is valid, creates a UsernamePasswordAuthenticationToken with the user's ID
     * and role authority, and places it in the SecurityContext.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain to continue processing
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);

            if (token != null && jwtService.isTokenValid(token)) {
                String userId = jwtService.extractUserId(token);
                List<String> roles = jwtService.extractRoles(token);

                // Create authorities with ROLE_ prefix as required by Spring Security
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) {
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }

                // Create authentication token with userId as principal
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            // Log error but continue filter chain to allow other error handlers to catch it
            logger.error("Cannot set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract the Bearer token from the Authorization header.
     * Expected format: Authorization: Bearer <token>
     *
     * @param request The HTTP request
     * @return The token string without "Bearer " prefix, or null if not found
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }
}

