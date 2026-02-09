package com.example.stock.controllers;

import com.example.stock.dto.AuthResponse;
import com.example.stock.dto.LoginRequest;
import com.example.stock.dto.RegisterRequest;
import com.example.stock.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PatchMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}


