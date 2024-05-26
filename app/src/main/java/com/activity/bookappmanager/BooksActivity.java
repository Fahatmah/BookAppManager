package com.activity.bookappmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

//    private static final String TAG = "BooksActivity";
//    private static final String API_KEY = "AIzaSyDoeB7mK6XTjFSNUakyxZ091_LtbaDMyek";

    FloatingActionButton abb;
    RecyclerView recyclerView;
    List<BookClass> bookList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    BookAdapter bookAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);

        BottomNavigationView tabBar = findViewById(R.id.tabNavigationView);
        tabBar.setSelectedItemId(R.id.booksTab);
        abb = findViewById(R.id.addBookButton);

        recyclerView = findViewById(R.id.booksRecyclerView);
        searchView = findViewById(R.id.search_bar);
        searchView.clearFocus();

        abb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BooksActivity.this, AddBookActivity.class);
                startActivity(i);
            }
        });

        tabBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.booksTab) {
                return true;
            } else if (itemId == R.id.addBooksTab) {
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.books_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // change default status bar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(BooksActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(BooksActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(BooksActivity.this, bookList);
        recyclerView.setAdapter(bookAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    BookClass dataClass = itemSnapshot.getValue(BookClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    bookList.add(dataClass);
                }
                bookAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    public void searchList(String text){
        ArrayList<BookClass> searchList = new ArrayList<>();
        for (BookClass bookClass: bookList){
            if (bookClass.getBookTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(bookClass);
            }
        }
        bookAdapter.searchDataList(searchList);
    }
}