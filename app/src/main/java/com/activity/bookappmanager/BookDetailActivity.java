package com.activity.bookappmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookDetailActivity extends AppCompatActivity {

    TextView bookDesc, bookTitle, bookAuthor, bookName;
    ImageView bookCover, backButton;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String bookCoverUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookTitle = findViewById(R.id.bookTitle);
        bookName = findViewById(R.id.bookName);
        bookDesc = findViewById(R.id.bookDescription);
        bookCover = findViewById(R.id.bookCover);
        bookAuthor = findViewById(R.id.bookAuthor);
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Change default status bar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }

        // Retrieve and display the book details passed via the intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookDesc.setText(bundle.getString("Description"));
            bookTitle.setText(bundle.getString("Title"));
            bookName.setText(bundle.getString("Title"));
            bookAuthor.setText(bundle.getString("Author"));
            key = bundle.getString("Key");
            bookCoverUrl = bundle.getString("Cover");
            Glide.with(this).load(bundle.getString("Cover")).into(bookCover);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(bookCoverUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(BookDetailActivity.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                        finish();
                    }
                });
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, UpdateBookActivity.class)
                        .putExtra("Title", bookTitle.getText().toString())
                        .putExtra("Description", bookDesc.getText().toString())
                        .putExtra("Author", bookAuthor.getText().toString())
                        .putExtra("Cover", bookCoverUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });

        // Set up the back button click listener to finish the activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Navigate back to the previous activity
        super.onBackPressed();
        finish();
    }
}
