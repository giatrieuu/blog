package com.example.blog.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blog.R;
import com.example.blog.presenter.AuthPresenter;
import com.example.blog.presenter.RolePresenter;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements AuthView, RoleView {
    private EditText emailEditText, passwordEditText;
    private ProgressDialog progressDialog;
    private AuthPresenter authPresenter;
    private RolePresenter rolePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signupRedirectButton = findViewById(R.id.signUpRedirectText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập...");

        authPresenter = new AuthPresenter(this);
        rolePresenter = new RolePresenter(this);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            authPresenter.login(email, password);
        });

        signupRedirectButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // Nếu user vẫn đăng nhập, kiểm tra role từ Firestore
            String uid = auth.getCurrentUser().getUid();
            rolePresenter.checkUserRole(uid);
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        hideLoading();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rolePresenter.checkUserRole(uid);
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
}
