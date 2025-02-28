package com.example.blog.model;

public class User {
    public String uid;
    public String email;
    public String role;

    public User() {} // Bắt buộc để Firebase parse dữ liệu

    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }
}
