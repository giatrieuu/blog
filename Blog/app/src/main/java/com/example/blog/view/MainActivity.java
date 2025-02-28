package com.example.blog.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blog.presenter.RolePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements RoleView {
    private FirebaseAuth auth;
    private RolePresenter rolePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        rolePresenter = new RolePresenter(this);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập, kiểm tra role và điều hướng
            String uid = currentUser.getUid();
            rolePresenter.checkUserRole(uid);
        } else {
            // Nếu chưa đăng nhập, quay về LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void navigateToAdmin() {
        startActivity(new Intent(this, AdminDashboardActivity.class));
        finish();
    }

    @Override
    public void navigateToUser() {
        startActivity(new Intent(this, UserActivity.class));
        finish();
    }

    @Override
    public void showLoading() {
        // Có thể thêm Splash Screen ở đây
    }

    @Override
    public void hideLoading() {
        // Không cần xử lý trong MainActivity
    }

    @Override
    public void showError(String message) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
