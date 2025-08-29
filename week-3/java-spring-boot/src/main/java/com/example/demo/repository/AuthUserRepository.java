package com.example.demo.repository;

import com.example.demo.model.AuthUser;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthUserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper rowMapper = (rs, rowNum) -> {
        AuthUser auth = new AuthUser();
        auth.setId(rs.getInt("id"));
        auth.setName(rs.getString("name"));
        auth.setEmail(rs.getString("email"));
        auth.setPasswordHash(rs.getString("password_hash"));
        auth.setRole(rs.getString("role"));
        return auth;
    };

    public AuthUserRepository(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }
    public AuthUser findByEmail(String email) {
        String sql = "SELECT id, name, email, password_hash, role from users WHERE email = ?";
        List<AuthUser> userList = jdbcTemplate.query(sql, rowMapper, email);
        return userList.isEmpty() ? null : userList.get(0);
    }
    public AuthUser insertNewUser(String name, String email, String passwordHash, String role) {
        jdbcTemplate.update("INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)",
                name, email, passwordHash, role);

        return findByEmail(email);
    }
}
