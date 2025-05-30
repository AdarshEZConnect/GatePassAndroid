package com.ranjit.gatepass;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ranjit.gatepass.fragments.ComplaintsFragment;
import com.ranjit.gatepass.fragments.HomeFragment;
import com.ranjit.gatepass.fragments.ViewPassFragment;

public class MainActivity extends AppCompatActivity {
    private final Fragment gatePassFragment = new HomeFragment();
    private final Fragment complaintFragment = new ComplaintsFragment();
    private final Fragment viewPassFragment = new ViewPassFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 50, systemBars.right, 0);
            return insets;
        });
        // Hide system bars
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }


        // Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,gatePassFragment).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = gatePassFragment;
            } else if (item.getItemId() == R.id.nav_complaints) {
                selectedFragment = complaintFragment;
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = viewPassFragment;
            } else {
                return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, selectedFragment)
                    .commit();

            return true;
        });
    }
}