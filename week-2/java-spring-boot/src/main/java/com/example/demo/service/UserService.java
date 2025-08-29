package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public User createUser(User user) {
        return userRepo.save(user);
    }
    public int updateUser(int id, User user) {
        return userRepo.update(id, user);
    }
    public User getUserById(int id) {
        return userRepo.findById(id);
    }
    public void deleteUser(int id) {
        userRepo.delete(id);
    }
}
