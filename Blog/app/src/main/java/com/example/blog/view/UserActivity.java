package com.example.blog.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blog.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView welcomeText = findViewById(R.id.user_welcome_text);
        Button logoutButton = findViewById(R.id.user_logout_button);

        // Xử lý sự kiện đăng xuất
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Đăng xuất khỏi Firebase
            startActivity(new Intent(UserActivity.this, LoginActivity.class)); // Quay lại màn hình login
            finish();
        });
    }
}
