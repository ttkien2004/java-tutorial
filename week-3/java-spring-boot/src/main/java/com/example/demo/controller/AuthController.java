package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.AuthUser;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService auth) {
        this.authService = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@Valid @RequestBody RegisterRequest req) {
        AuthUser created = authService.register(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
