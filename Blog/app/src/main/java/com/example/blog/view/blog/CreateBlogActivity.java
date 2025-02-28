package com.example.blog.view.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.blog.R;
import com.example.blog.presenter.BlogPresenter;
import com.example.blog.view.blog.BlogView;


public class CreateBlogActivity extends AppCompatActivity implements BlogView {
    private ImageView blogImageView;
    private EditText blogTitleInput, blogShortDescInput, blogLongDescInput;
    private Button selectImageButton, saveBlogButton;
    private Uri imageUri = null;
    private BlogPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        // Khởi tạo UI
        blogImageView = findViewById(R.id.blogImageView);
        blogTitleInput = findViewById(R.id.blogTitleInput);
        blogShortDescInput = findViewById(R.id.blogShortDescInput);
        blogLongDescInput = findViewById(R.id.blogLongDescInput);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveBlogButton = findViewById(R.id.saveBlogButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang lưu blog...");

        // Khởi tạo Presenter
        presenter = new BlogPresenter(this);

        // Chọn ảnh
        selectImageButton.setOnClickListener(v -> selectImage());

        // Lưu blog
        saveBlogButton.setOnClickListener(v -> saveBlog());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    blogImageView.setImageURI(imageUri);
                }
            });

    private void saveBlog() {
        String title = blogTitleInput.getText().toString().trim();
        String shortDesc = blogShortDescInput.getText().toString().trim();
        String longDesc = blogLongDescInput.getText().toString().trim();

        if (title.isEmpty() || shortDesc.isEmpty() || longDesc.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        presenter.createBlog(title, shortDesc, longDesc, imageUri);
    }

    @Override
    public void onBlogSaved() {
        progressDialog.dismiss();
        Toast.makeText(this, "Blog đã lưu!", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public void onBlogDeleted() {
        // Không cần thực hiện gì ở đây vì CreateBlogActivity không cần xóa blog
    }

    @Override
    public void showError(String message) {
        progressDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
