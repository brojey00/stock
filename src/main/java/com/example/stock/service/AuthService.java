package com.example.stock.service;

import com.example.stock.dao.entities.Role;
import com.example.stock.dao.entities.User;
import com.example.stock.dao.repositories.UserRepository;
import com.example.stock.dto.AuthResponse;
import com.example.stock.dto.LoginRequest;
import com.example.stock.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RestClient restClient = RestClient.create();

    @Value("${app.keycloak.server-url:http://localhost:8180}")
    private String keycloakServerUrl;

    @Value("${app.keycloak.realm:AppRealm}")
    private String keycloakRealm;

    @Value("${app.keycloak.client-id:stock-app}")
    private String keycloakClientId;

    @Value("${app.keycloak.client-secret:}")
    private String keycloakClientSecret;

    @Value("${app.keycloak.admin.realm:master}")
    private String keycloakAdminRealm;

    @Value("${app.keycloak.admin.client-id:admin-cli}")
    private String keycloakAdminClientId;

    @Value("${app.keycloak.admin.username:admin}")
    private String keycloakAdminUsername;

    @Value("${app.keycloak.admin.password:admin}")
    private String keycloakAdminPassword;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered!");
        }

        Role role = request.getRole() == null ? Role.MANAGER : request.getRole();
        String adminAccessToken = requestToken(
                keycloakAdminRealm,
                keycloakAdminClientId,
                null,
                keycloakAdminUsername,
                keycloakAdminPassword
        ).accessToken();

        String keycloakUserId = createKeycloakUser(adminAccessToken, request);
        assignRealmRole(adminAccessToken, keycloakUserId, role.name());

        User user = new User();
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setKeycloakId(keycloakUserId);
        User savedUser = userRepository.save(user);


        TokenPayload userTokens = requestToken(
                keycloakRealm,
                keycloakClientId,
                keycloakClientSecret,
                request.getEmail(),
                request.getPassword()
        );
        return buildAuthResponse(savedUser, userTokens, "User registered successfully!");
    }

    public AuthResponse login(LoginRequest request) {
        TokenPayload tokenPayload = requestToken(
                keycloakRealm,
                keycloakClientId,
                keycloakClientSecret,
                request.getEmail(),
                request.getPassword()
        );

        String adminAccessToken = requestToken(
                keycloakAdminRealm,
                keycloakAdminClientId,
                null,
                keycloakAdminUsername,
                keycloakAdminPassword
        ).accessToken();

        String keycloakUserId = findKeycloakUserIdByEmail(adminAccessToken, request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> createLocalUserFromKeycloak(request.getEmail(), keycloakUserId));

        return buildAuthResponse(user, tokenPayload, "Login successful!");
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        requestToken(keycloakRealm, keycloakClientId, keycloakClientSecret, email, oldPassword);
        resetPassword(email, newPassword);
    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        String adminAccessToken = requestToken(
                keycloakAdminRealm,
                keycloakAdminClientId,
                null,
                keycloakAdminUsername,
                keycloakAdminPassword
        ).accessToken();

        String keycloakUserId = findKeycloakUserIdByEmail(adminAccessToken, email);
        Map<String, Object> credential = Map.of(
                "type", "password",
                "value", newPassword,
                "temporary", false
        );

        restClient.put()
                .uri(keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + keycloakUserId + "/reset-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(credential)
                .retrieve()
                .toBodilessEntity();
    }

    private String createKeycloakUser(String adminAccessToken, RegisterRequest request) {
        Map<String, Object> credentials = Map.of(
                "type", "password",
                "value", request.getPassword(),
                "temporary", false
        );

        Map<String, Object> userRepresentation = Map.of(
                "username", request.getEmail(),
                "email", request.getEmail(),
                "enabled", true,
                "firstName", request.getFullname(),
                "credentials", List.of(credentials)
        );

        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri(keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRepresentation)
                    .retrieve()
                    .toBodilessEntity();

            URI location = response.getHeaders().getLocation();
            if (location == null) {
                throw new IllegalStateException("Keycloak did not return created user location");
            }

            String[] segments = location.getPath().split("/");
            return segments[segments.length - 1];
        } catch (HttpClientErrorException.Conflict conflict) {
            throw new IllegalStateException("Email already exists in Keycloak", conflict);
        }
    }

    @SuppressWarnings("unchecked")
    private void assignRealmRole(String adminAccessToken, String keycloakUserId, String roleName) {
        Map<String, Object> roleRepresentation = restClient.get()
                .uri(keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/roles/" + roleName)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .retrieve()
                .body(Map.class);

        restClient.post()
                .uri(keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + keycloakUserId + "/role-mappings/realm")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonList(roleRepresentation))
                .retrieve()
                .toBodilessEntity();
    }

    @SuppressWarnings("unchecked")
    private String findKeycloakUserIdByEmail(String adminAccessToken, String email) {
        List<Map<String, Object>> users = restClient.get()
                .uri(keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users?email=" + email)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .retrieve()
                .body(List.class);

        if (users == null || users.isEmpty()) {
            throw new IllegalStateException("User not found in Keycloak");
        }

        Object idValue = users.get(0).get("id");
        if (idValue == null) {
            throw new IllegalStateException("User id missing in Keycloak response");
        }
        return idValue.toString();
    }

    private TokenPayload requestToken(String realm, String clientId, String clientSecret, String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("username", username);
        form.add("password", password);

        try {
            TokenPayload tokenPayload = restClient.post()
                    .uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(TokenPayload.class);

            if (tokenPayload == null || tokenPayload.accessToken() == null || tokenPayload.accessToken().isBlank()) {
                throw new IllegalStateException("Token response from Keycloak is empty");
            }
            return tokenPayload;
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.BAD_REQUEST || exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadCredentialsException("Invalid email or password");
            }
            throw exception;
        }
    }

    private AuthResponse buildAuthResponse(User user, TokenPayload tokenPayload, String message) {
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getFullname())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(tokenPayload.accessToken())
                .refreshToken(tokenPayload.refreshToken())
                .tokenType(tokenPayload.tokenType())
                .expiresIn(tokenPayload.expiresIn())
                .message(message)
                .build();
    }

    private record TokenPayload(
            @com.fasterxml.jackson.annotation.JsonProperty("access_token") String accessToken,
            @com.fasterxml.jackson.annotation.JsonProperty("refresh_token") String refreshToken,
            @com.fasterxml.jackson.annotation.JsonProperty("token_type") String tokenType,
            @com.fasterxml.jackson.annotation.JsonProperty("expires_in") Long expiresIn
    ) {
    }
    private User createLocalUserFromKeycloak(String email, String keycloakUserId) {
        User user = new User();
        user.setEmail(email);
        user.setFullname(email); // or fetch from Keycloak if needed
        user.setKeycloakId(keycloakUserId);

        // Default role (important)
        user.setRole(Role.MANAGER);

        return userRepository.save(user);
    }
}