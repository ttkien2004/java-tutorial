package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.PageUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.PageUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageUserService {
    private final PageUserRepository pageUserRepo;

    public PageUserService(PageUserRepository pageUserRepo) {
        this.pageUserRepo = pageUserRepo;
    }

    public PageUser findPage(int page, int size) {
        return pageUserRepo.findByPage(page, size);
    }

}
