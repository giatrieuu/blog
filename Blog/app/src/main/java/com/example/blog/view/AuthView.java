package com.example.blog.view;

public interface AuthView {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void onAuthSuccess(String message);
}
