package com.example.demo.model;

import java.util.List;

public class PageUser {
    private List<User> users;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private int totalElements;

    public PageUser(List<User> users, int totalPages, int currentPage, int pageSize, int totalElements) {
        this.users = users;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    public List<User> getContent() {
        return users;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return currentPage;
    }

    public int getSize() {
        return pageSize;
    }

    public int getTotalElements() {
        return totalElements;
    }
}
