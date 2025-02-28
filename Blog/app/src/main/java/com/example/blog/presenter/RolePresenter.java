package com.example.blog.presenter;

import android.util.Log;
import com.example.blog.repository.FirebaseRepository;
import com.example.blog.view.RoleView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class    RolePresenter {
    private final RoleView view;
    private final FirebaseRepository repository;
    private static final String TAG = "RolePresenter";

    public RolePresenter(RoleView view) {
        this.view = view;
        this.repository = new FirebaseRepository();
    }

    public void checkUserRole(String uid) {
        view.showLoading();
        Log.d(TAG, "Bắt đầu kiểm tra role cho UID: " + uid);

        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot snapshot = task.getResult();
                        String role = snapshot.getString("role");

                        if ("admin".equals(role)) {
                            Log.d(TAG, "Admin detected, navigating...");
                            view.navigateToAdmin();
                        } else {
                            Log.d(TAG, "User detected, navigating...");
                            view.navigateToUser();
                        }
                    } else {
                        Log.e(TAG, "Không tìm thấy thông tin người dùng hoặc lỗi Firestore");
                        view.showError("Không tìm thấy thông tin người dùng");
                    }
                }).addOnFailureListener(e -> {
                    view.hideLoading();
                    Log.e(TAG, "Lỗi Firestore: " + e.getMessage());
                    view.showError("Lỗi khi lấy dữ liệu: " + e.getMessage());
                });
    }
}
