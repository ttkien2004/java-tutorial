package com.example.demo.repository;

import com.example.demo.model.User;
import com.example.demo.model.PageUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PageUserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User newUser = new User();
        newUser.setId(rs.getInt("id"));
        newUser.setName(rs.getString("name"));
        newUser.setEmail(rs.getString("email"));
        return newUser;
    };
    public PageUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public PageUser findByPage(int page, int size) {
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;
        List<User> users = jdbcTemplate.query(sql, rowMapper, size, offset);
        String countSql = "SELECT COUNT(*) FROM users";
        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageUser(users, totalPages, page, size, totalElements);
    }
}
