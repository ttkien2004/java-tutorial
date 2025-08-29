package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import com.example.demo.model.PageUser;
import com.example.demo.service.PageUserService;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-page")
public class PageUserController {
    private final PageUserService pageUserService;

    public PageUserController(PageUserService pageUserService) {
        this.pageUserService = pageUserService;
    }

    @GetMapping("/page")
    public ResponseEntity<PageUser> getPage(@RequestParam int page, @RequestParam int size) {
        PageUser userPage = pageUserService.findPage(page, size);
        if (userPage != null) {
            return ResponseEntity.ok(userPage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
