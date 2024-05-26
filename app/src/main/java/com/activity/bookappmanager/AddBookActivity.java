package com.activity.bookappmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class AddBookActivity extends AppCompatActivity {

    ImageView bookCover, backButton;
    Button uploadButton;
    EditText bookName, bookDesc, bookAuthor;
    String bookCoverURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);

        BottomNavigationView tabBar = findViewById(R.id.tabNavigationView);
        tabBar.setSelectedItemId(R.id.addBooksTab);
        bookCover = findViewById(R.id.uploadBookCoverField);
        bookName = findViewById(R.id.uploadBookNameField);
        bookDesc = findViewById(R.id.uploadBookDescriptionField);
        bookAuthor = findViewById(R.id.uploadAuthorNameField);
        uploadButton = findViewById(R.id.uploadBookButton);
        backButton = findViewById(R.id.backButton);

        tabBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.booksTab) {
                startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.addBooksTab) {
                return true;
            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_book_main), (v, insets) -> {
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

        // Setup activity result launcher for image picking
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            bookCover.setImageURI(uri);
                        } else {
                            Toast.makeText(AddBookActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Setup click listener for the book cover to pick an image
        bookCover.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        // Setup click listener for the upload button to save data
        uploadButton.setOnClickListener(v -> saveData());

        // Setup click listener for the back button to go back to the previous activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void saveData() {
        // Getting a reference to Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Book Covers").child(uri.getLastPathSegment());

        // Creating an AlertDialog to show while uploading
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Uploading the file to the specified location in Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri urlImage = uriTask.getResult();
            bookCoverURL = urlImage.toString();
            uploadData();
            dialog.dismiss();
        }).addOnFailureListener(e -> dialog.dismiss());
    }

    // Uploads the metadata (title, desc, cover, bookCoverURL) associated
    public void uploadData() {
        String title = bookName.getText().toString();
        String desc = bookDesc.getText().toString();
        String cover = bookAuthor.getText().toString();

        BookClass dataClass = new BookClass(title, desc, cover, bookCoverURL);

        // Generate a timestamp which is the "currentDate" to be used as the unique key for storing the date in Firebase Realtime Database
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Store the 'BookClass' object in the Firebase Realtime Database under the "Books" node with the timestamp as the key
        FirebaseDatabase.getInstance().getReference("Books").child(currentDate).setValue(dataClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddBookActivity.this, "Book Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(AddBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
