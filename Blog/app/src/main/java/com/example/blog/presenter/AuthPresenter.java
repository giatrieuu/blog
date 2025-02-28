package com.example.blog.presenter;

import android.util.Log;

import com.example.blog.model.User;
import com.example.blog.utils.Constants;
import com.example.blog.repository.FirebaseRepository;
import com.example.blog.view.AuthView;
import com.google.firebase.auth.FirebaseUser;

public class AuthPresenter {
    private final AuthView view;
    private final FirebaseRepository repository;
    private static final String TAG = "AuthPresenter";

    public AuthPresenter(AuthView view) {
        this.view = view;
        this.repository = new FirebaseRepository();
    }

    public void signUp(String email, String password) {
        view.showLoading();
        repository.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = repository.getAuth().getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            String role = email.equals(Constants.ADMIN_EMAIL) ? Constants.ROLE_ADMIN : Constants.ROLE_USER;

                            User user = new User(uid, email, role);
                            repository.saveUserToDatabase(user);

                            Log.d(TAG, "Đăng ký thành công: " + email);
                            view.onAuthSuccess("Đăng ký thành công!");
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        Log.e(TAG, "Đăng ký thất bại: " + errorMessage);
                        view.showError("Đăng ký thất bại: " + errorMessage);
                    }
                });
    }

    public void login(String email, String password) {
        view.showLoading();
        repository.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        view.onAuthSuccess("Đăng nhập thành công!");
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        Log.e(TAG, "Đăng nhập thất bại: " + errorMessage);
                        view.showError("Đăng nhập thất bại: " + errorMessage);
                    }
                });
    }
}
