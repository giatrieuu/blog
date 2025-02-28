package com.example.blog.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blog.R;
import com.example.blog.adapters.BlogAdapter;
import com.example.blog.model.Blog;
import com.example.blog.presenter.BlogPresenter;
import com.example.blog.view.blog.BlogView;
import com.example.blog.view.blog.CreateBlogActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements BlogView {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private BlogAdapter adapter;
    private List<Blog> blogList = new ArrayList<>();
    private String currentUserId;
    private boolean isAdmin = false;
    private BlogPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        presenter = new BlogPresenter(this);
        currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        recyclerView = findViewById(R.id.blogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter với xóa Blog
        adapter = new BlogAdapter(this, blogList, new BlogAdapter.OnBlogClickListener() {
            @Override
            public void onBlogClick(Blog blog) {
                Toast.makeText(AdminDashboardActivity.this, "Chọn blog: " + blog.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditBlog(Blog blog) {}

            @Override
            public void onDeleteBlog(Blog blog) {
                confirmDeleteBlog(blog);
            }
        });
        recyclerView.setAdapter(adapter);

        checkAdminRole();

        // Nút thêm blog
        Button createBlogButton = findViewById(R.id.createBlogButton);
        createBlogButton.setOnClickListener(v -> startActivity(new Intent(this, CreateBlogActivity.class)));

        // Nút Đăng Xuất
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> logout());
    }

    // Kiểm tra quyền admin
    private void checkAdminRole() {
        if (currentUserId == null) {
            Toast.makeText(this, "Lỗi xác thực! Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            logout();
            return;
        }

        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && "admin".equals(documentSnapshot.getString("role"))) {
                        isAdmin = true;
                        loadBlogs();
                    } else {
                        Toast.makeText(this, "Bạn không có quyền truy cập!", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi kiểm tra quyền!", Toast.LENGTH_SHORT).show();
                    logout();
                });
    }

    // Load danh sách blog real-time
    private void loadBlogs() {
        db.collection("blogs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AdminDashboardActivity.this, "Lỗi khi tải blogs!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    blogList.clear();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        Blog blog = document.toObject(Blog.class);
                        blogList.add(blog);
                    }
                    adapter.setBlogs(blogList);
                }
            }
        });
    }

    // Xác nhận trước khi xóa blog
    private void confirmDeleteBlog(Blog blog) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Blog")
                .setMessage("Bạn có chắc chắn muốn xóa blog này?")
                .setPositiveButton("Xóa", (dialog, which) -> presenter.deleteBlog(blog))
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xử lý đăng xuất
    private void logout() {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Callbacks từ Presenter
    @Override
    public void onBlogSaved() {}

    @Override
    public void onBlogDeleted() {
        Toast.makeText(this, "Blog đã bị xóa!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
