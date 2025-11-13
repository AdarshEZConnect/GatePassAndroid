package com.ranjit.gatepass;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ranjit.gatepass.fragments.ComplaintsFragment;
import com.ranjit.gatepass.fragments.HomeFragment;
import com.ranjit.gatepass.fragments.ProfileFragment;
import com.ranjit.gatepass.fragments.ViewPassFragment;

public class MainActivity extends AppCompatActivity {
    private final Fragment gatePassFragment = new HomeFragment();
    private final Fragment complaintFragment = new ComplaintsFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final Fragment viewPassFragment = new ViewPassFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 50, systemBars.right,0);
            return insets;
        });

        // --- Core Security Implementation ---
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );


        // Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,gatePassFragment).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = gatePassFragment;
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = profileFragment;
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