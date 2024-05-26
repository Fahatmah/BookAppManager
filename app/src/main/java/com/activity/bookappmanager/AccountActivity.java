package com.activity.bookappmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        BottomNavigationView tabBar = findViewById(R.id.tabNavigationView);
//        tabBar.setSelectedItemId(R.id.accountTab);

        tabBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.booksTab) {
                startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.addBooksTab) {
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
//            else if (itemId == R.id.starredBooksTab) {
//                startActivity(new Intent(getApplicationContext(), StarredBooksActivity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//                return true;
//            } else if (itemId == R.id.accountTab) {
//                return true;
//            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.account_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}