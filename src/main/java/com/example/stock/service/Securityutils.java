package com.example.stock.service;

import com.example.stock.dao.entities.User;
import com.example.stock.dao.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Securityutils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getPrincipal();

        if (!(principal instanceof Jwt jwt)) {
            throw new AccessDeniedException("No JWT token found in security context");
        }

        String sub = jwt.getSubject();
        if (sub == null) {
            throw new AccessDeniedException("JWT is missing 'sub' claim");
        }

        String keycloakId;
        try {
            keycloakId = sub;
        } catch (IllegalArgumentException e) {
            throw new AccessDeniedException("JWT 'sub' is not a valid UUID: " + sub);
        }

        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user has no local profile (keycloakId=" + sub + "). Contact an admin."));
    }
}