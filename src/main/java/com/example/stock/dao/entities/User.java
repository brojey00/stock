package com.example.stock.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullname;
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private String keycloakId;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role = Role.MANAGER;
    @ManyToOne
    @JoinColumn(name = "entrepot_id", nullable = true)
    private Entrepot entrepotAssigne;


}
