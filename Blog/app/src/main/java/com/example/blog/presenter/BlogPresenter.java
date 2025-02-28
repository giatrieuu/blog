package com.example.blog.presenter;

import android.net.Uri;
import com.example.blog.model.Blog;
import com.example.blog.view.blog.BlogView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class BlogPresenter {
    private final BlogView view;
    private final FirebaseFirestore db;
    private final StorageReference storageRef;

    public BlogPresenter(BlogView view) {
        this.view = view;
        this.db = FirebaseFirestore.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference("blog_images");
    }

    // 🔹 Tạo Blog
    public void createBlog(String title, String shortDesc, String longDesc, Uri imageUri) {
        if (imageUri != null) {
            // Upload ảnh lên Firebase Storage
            String imageName = UUID.randomUUID().toString();
            StorageReference imageRef = storageRef.child(imageName);

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveBlogToFirestore(title, shortDesc, longDesc, uri.toString());
                });
            }).addOnFailureListener(e -> view.showError("Lỗi khi tải ảnh!"));
        } else {
            saveBlogToFirestore(title, shortDesc, longDesc, "");
        }
    }

    private void saveBlogToFirestore(String title, String shortDesc, String longDesc, String imageUrl) {
        String blogId = UUID.randomUUID().toString();
        Blog blog = new Blog(blogId, title, shortDesc, longDesc, imageUrl);

        db.collection("blogs").document(blogId).set(blog)
                .addOnSuccessListener(unused -> view.onBlogSaved())
                .addOnFailureListener(e -> view.showError("Lỗi khi lưu blog!"));
    }

    // 🔹 Xóa Blog (Gồm cả ảnh trong Firebase Storage nếu có)
    public void deleteBlog(Blog blog) {
        if (blog.getImageUrl() != null && !blog.getImageUrl().isEmpty()) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(blog.getImageUrl());
            imageRef.delete()
                    .addOnSuccessListener(unused -> deleteBlogFromFirestore(blog))
                    .addOnFailureListener(e -> view.showError("Lỗi khi xóa ảnh!"));
        } else {
            deleteBlogFromFirestore(blog);
        }
    }

    // Xóa blog khỏi Firestore
    private void deleteBlogFromFirestore(Blog blog) {
        db.collection("blogs").document(blog.getId())
                .delete()
                .addOnSuccessListener(unused -> view.onBlogDeleted())
                .addOnFailureListener(e -> view.showError("Lỗi khi xóa blog!"));
    }
}
