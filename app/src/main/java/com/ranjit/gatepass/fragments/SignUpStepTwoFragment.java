package com.ranjit.gatepass.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.activity.LoginActivity;
import com.ranjit.gatepass.models.RegisterRequest;
import com.ranjit.gatepass.models.SignUpViewModel;
import com.ranjit.gatepass.services.ApiClient;
import com.ranjit.gatepass.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStepTwoFragment extends Fragment {

    private SignUpViewModel viewModel;
    private TextInputEditText emailField, passwordField;
    private ImageView imagePreview;
    private MaterialButton btnBack, btnSubmit;

    public SignUpStepTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_step_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        // Views
        emailField = view.findViewById(R.id.input_email);
        passwordField = view.findViewById(R.id.input_password);
        imagePreview = view.findViewById(R.id.image_preview);
        btnBack = view.findViewById(R.id.btn_back);
        btnSubmit = view.findViewById(R.id.btn_submit);
        Spinner genderSpinner = view.findViewById(R.id.spinner_gender);

        // Gender Spinner
        List<String> genderOptions = new ArrayList<>();
        genderOptions.add("Select Gender");
        genderOptions.add("Male");
        genderOptions.add("Female");
        genderOptions.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                genderOptions
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Load image from ViewModel (URL using Glide)
        if (viewModel.profileImageUri.getValue() != null) {
            Glide.with(requireContext())
                    .load(viewModel.profileImageUri.getValue())
                    .placeholder(R.drawable.man)
                    .error(R.drawable.man)
                    .into(imagePreview);
        }

        // Back button
        btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        // Submit button
        btnSubmit.setOnClickListener(v -> {
            String email = emailField.getText() != null ? emailField.getText().toString().trim() : "";
            String password = passwordField.getText() != null ? passwordField.getText().toString().trim() : "";

            if (email.isEmpty()) {
                emailField.setError("Email is required");
                emailField.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordField.setError("Password is required");
                passwordField.requestFocus();
                return;
            }

            String selectedGender = genderSpinner.getSelectedItem().toString();

            // Save to ViewModel
            viewModel.email.setValue(email);
            viewModel.password.setValue(password);

            // Prepare registration request
            RegisterRequest request = new RegisterRequest(
                    viewModel.name.getValue(),
                    email,
                    password,
                    selectedGender,
                    viewModel.profileImageUri.getValue() != null ? String.valueOf(viewModel.profileImageUri.getValue()) : "profile.png",
                    viewModel.userContact.getValue(),
                    viewModel.parentContact.getValue(),
                    2,
                    viewModel.departmentId.getValue()
            );

            // API call
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.registerUser(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(getContext(), "Registration failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
