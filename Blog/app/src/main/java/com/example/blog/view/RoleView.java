package com.example.blog.view;

public interface RoleView {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void navigateToAdmin();
    void navigateToUser();
}
