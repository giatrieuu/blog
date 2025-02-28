package com.example.blog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.blog.R;
import com.example.blog.model.Blog;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    private final Context context;
    private List<Blog> blogList;
    private final OnBlogClickListener listener;

    public interface OnBlogClickListener {
        void onBlogClick(Blog blog);
        void onEditBlog(Blog blog); // Xử lý Chỉnh sửa
        void onDeleteBlog(Blog blog); // Xử lý Xóa
    }

    public BlogAdapter(Context context, List<Blog> blogList, OnBlogClickListener listener) {
        this.context = context;
        this.blogList = blogList;
        this.listener = listener;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogList = blogs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.title.setText(blog.getTitle());
        holder.shortDescription.setText(blog.getShortDesc());

        // Load hình ảnh bằng Glide
        Glide.with(context).load(blog.getImageUrl()).into(holder.imageView);

        // Click vào blog
        holder.itemView.setOnClickListener(v -> listener.onBlogClick(blog));

        // Xử lý click vào dấu ba chấm
        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.menuButton);
            popup.inflate(R.menu.blog_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_edit) {
                    listener.onEditBlog(blog);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    listener.onDeleteBlog(blog);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView title, shortDescription;
        ImageView imageView, menuButton;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.blogTitle);
            shortDescription = itemView.findViewById(R.id.blogShortDesc);
            imageView = itemView.findViewById(R.id.blogImage);
            menuButton = itemView.findViewById(R.id.menuButton); // Dấu ba chấm
        }
    }
}
