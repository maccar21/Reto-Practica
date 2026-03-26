package com.bancolombia.chocolatinazo.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service responsible for JWT token generation and validation.
 * Uses HMAC SHA-256 algorithm with a Base64 encoded secret key.
 * Token contains userId as subject and includes role and username as claims.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate a JWT token for the given user.
     * @param userId The UUID of the authenticated user
     * @param roles List of role names (PLAYER, AUDITOR, ADMIN)
     * @param username The username of the authenticated user
     * @return A signed JWT token string
     */
    public String generateToken(UUID userId, List<String> roles, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("username", username);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract the user ID (subject) from a JWT token.
     * @param token The JWT token string
     * @return The user ID extracted from the subject claim
     */
    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extract the roles list from a JWT token.
     * @param token The JWT token string
     * @return List of role names
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) extractAllClaims(token).get("roles");
    }

    /**
     * Extract the username from a JWT token.
     * @param token The JWT token string
     * @return The username claim value
     */
    public String extractUsername(String token) {
        return (String) extractAllClaims(token).get("username");
    }

    /**
     * Validate if the JWT token is valid (not expired, proper signature).
     * @param token The JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if a JWT token is expired.
     * Used by the filter to provide a specific error message for expired tokens.
     *
     * @param token The JWT token string
     * @return true if the token is expired, false if invalid for other reasons
     */
    public boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extract all claims from the JWT token.
     * @param token The JWT token string
     * @return The Claims object containing all token data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Get the signing key from the Base64 encoded secret.
     * Converts the Base64 secret from application.properties into a SecretKey.
     * @return The SecretKey used for signing and validating tokens
     */
    private SecretKey getSigningKey() {
        byte[] decodedSecret = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedSecret);
    }
}

