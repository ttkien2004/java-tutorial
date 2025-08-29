package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.AuthUser;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthUserRepository authUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthUserRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authUserRepo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthUser register(RegisterRequest req) {
        String hash = passwordEncoder.encode(req.getPassword());

        return authUserRepo.insertNewUser(req.getName(), req.getEmail(), hash, "USER");
    }

    public String login(LoginRequest req) {
        AuthUser user = authUserRepo.findByEmail(req.getEmail());
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
