package com.example.blog.repository;

import android.util.Log;
import com.example.blog.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {
    private static final String TAG = "FirebaseRepository";
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void saveUserToDatabase(User user) {
        firestore.collection("users")
                .document(user.uid)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Người dùng đã được lưu vào Firestore: " + user.uid))
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi lưu người dùng vào Firestore: " + e.getMessage()));
    }
}
