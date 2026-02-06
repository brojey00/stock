package com.example.stock.dto;

import com.example.stock.dao.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Size;
@Data
public class RegisterRequest {

    @NotBlank(message = "Fullname is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private Role role = Role.MANAGER; // Default role
}