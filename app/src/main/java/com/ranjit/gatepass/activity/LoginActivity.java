package com.ranjit.gatepass.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ranjit.gatepass.MainActivity;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.models.LoginRequest;
import com.ranjit.gatepass.models.LoginResponse;
import com.ranjit.gatepass.models.User;
import com.ranjit.gatepass.services.ApiClient;
import com.ranjit.gatepass.services.AuthService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView signUpLink;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        // Init views
        emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.btn_login);
        signUpLink = findViewById(R.id.tv_signup);
        sharedPreferences = getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);

        // Login button logic
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
            String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password required");
                return;
            }

            // 🔐 Real API login
            loginWithApi(email, password);
        });

        // Navigate to Sign Up
        signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void loginWithApi(String email, String password) {
        AuthService authService = ApiClient.getClient().create(AuthService.class);
        LoginRequest request = new LoginRequest(email, password);
        Call<LoginResponse> call = authService.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().getUser();

                    // ✅ Store in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", user.getJwtToken());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("role", user.getRoleName());
                    editor.putInt("userId", user.getUserId());
                    editor.putString("parentMobile", user.getParentContact());
                    editor.putString("userMobile", user.getUserContact());
                    editor.putString("departmentName", user.getDepartmentName());
                    editor.putString("gender", user.getGender());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
