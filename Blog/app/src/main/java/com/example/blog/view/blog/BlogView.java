package com.example.blog.view.blog;

import com.example.blog.model.Blog;
import java.util.List;

public interface BlogView {
    void onBlogSaved();
    void onBlogDeleted();
    void showError(String message);
}