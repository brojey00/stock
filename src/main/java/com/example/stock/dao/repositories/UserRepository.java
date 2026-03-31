package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByKeycloakId(UUID keycloakId);
}
