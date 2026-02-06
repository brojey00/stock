package com.example.stock.dto;

import com.example.stock.dao.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Integer id;
    private String username;
    private String email;
    private Role role;
    private String message;
}