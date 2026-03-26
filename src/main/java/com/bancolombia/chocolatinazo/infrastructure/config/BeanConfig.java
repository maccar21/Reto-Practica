package com.bancolombia.chocolatinazo.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring configuration class for defining application-wide beans.
 * Centralizes bean definitions that don't belong to a specific layer.
 */
@Configuration
public class BeanConfig {

    /**
     * Provides a BCrypt password encoder for securely hashing user passwords.
     * Used by {@link com.bancolombia.chocolatinazo.application.service.AuthService} during registration and login.
     *
     * @return PasswordEncoder using BCrypt hashing algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
