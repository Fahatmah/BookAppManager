package com.activity.bookappmanager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
public class StatusBar extends AppCompatActivity {

    /*
    TRIED TO APPLY IT ACROSS ACTIVITIES, DID NOT WORK
    SO I MANUALLY ADDED THIS BLOCK OF CODE ACROSS
    ACTIVTIES
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }
    }
}

