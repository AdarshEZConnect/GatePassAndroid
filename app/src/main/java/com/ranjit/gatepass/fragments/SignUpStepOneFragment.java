package com.ranjit.gatepass.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.models.Department;
import com.ranjit.gatepass.models.SignUpViewModel;
import com.ranjit.gatepass.services.ApiClient;
import com.ranjit.gatepass.services.ApiService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SignUpStepOneFragment extends Fragment {
    private ProgressBar progressBar;

    private SignUpViewModel viewModel;
    private ImageView imageView;
    private Uri selectedImageUri;
    private Spinner spinnerDepartments;
    private List<Department> departmentList = new ArrayList<>();
    private MaterialButton nextButton;
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imageView.setImageURI(uri);
                }
            });

    public SignUpStepOneFragment() {
        super(R.layout.fragment_sign_up_step_one);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        EditText nameField = view.findViewById(R.id.input_name);
        EditText userContactField = view.findViewById(R.id.user_input_contact);
        EditText parentContactField = view.findViewById(R.id.parent_input_contact);
        imageView = view.findViewById(R.id.image_view);
        spinnerDepartments = view.findViewById(R.id.spinner_departments);
        nextButton = view.findViewById(R.id.btn_next);
        progressBar = view.findViewById(R.id.upload_progress);

        loadDepartments();
        imageView.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        nextButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String userContact = userContactField.getText().toString().trim();
            String parentContact = parentContactField.getText().toString().trim();

            if (name.isEmpty()) {
                nameField.setError("Name is required");
                return;
            }
            if (userContact.isEmpty()) {
                userContactField.setError("User contact is required");
                return;
            }
            if (parentContact.isEmpty()) {
                parentContactField.setError("Parent contact is required");
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(getContext(), "Select a profile image", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedPosition = spinnerDepartments.getSelectedItemPosition();
            if (selectedPosition == Spinner.INVALID_POSITION || departmentList.isEmpty()) {
                Toast.makeText(getContext(), "Please select a department", Toast.LENGTH_SHORT).show();
                return;
            }

            Department selectedDepartment = departmentList.get(selectedPosition);
            try {
                File imageFile = createFileFromUri(requireContext(), selectedImageUri, getFileNameFromUri(requireContext(), selectedImageUri));
                uploadImageToImageKit(imageFile, imageUrl -> {
                    viewModel.name.setValue(name);
                    viewModel.userContact.setValue(userContact);
                    viewModel.parentContact.setValue(parentContact);
                    viewModel.profileImageUri.setValue(Uri.parse(imageUrl));
                    viewModel.departmentId.setValue(selectedDepartment.getDepartmentId());

                    getParentFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right
                            )
                            .replace(R.id.fragment_container, new SignUpStepTwoFragment())
                            .addToBackStack(null)
                            .commit();
                });
            } catch (Exception e) {
                Toast.makeText(getContext(), "File creation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDepartments() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getDepartments().enqueue(new retrofit2.Callback<List<Department>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Department>> call, retrofit2.Response<List<Department>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    departmentList = response.body();

                    ArrayAdapter<Department> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, departmentList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDepartments.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Department>> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading departments", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImageToImageKit(File imageFile, OnUploadCompleteListener listener) {
        progressBar.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))  // Optional: Force HTTP/1.1
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(),
                        RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                .addFormDataPart("fileName", imageFile.getName())
                .build();

        Request request = new Request.Builder()
                .url("https://upload.imagekit.io/api/v1/files/upload")
                .addHeader("Authorization", "Basic cHJpdmF0ZV8ya0JNczFDTStFQXc1eFB6WmZtVUZrYWkwTUk9Og==")  // <- YOUR private key base64
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    nextButton.setEnabled(true);
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String error = response.body() != null ? response.body().string() : "Unknown error";
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        nextButton.setEnabled(true);
                        Toast.makeText(getContext(), "Upload error: " + error, Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    String url = json.getString("url");

                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        nextButton.setEnabled(true);
                        viewModel.profileImageUri.setValue(Uri.parse(url));  // ✅ Store URL
                        listener.onUploadComplete(url);                      // 🔁 Proceed flow
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        nextButton.setEnabled(true);
                        Toast.makeText(getContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }

    private interface OnUploadCompleteListener {
        void onUploadComplete(String imageUrl);
    }

    private File createFileFromUri(Context context, Uri uri, String fileName) throws Exception {
        File file = new File(context.getCacheDir(), fileName);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return file;
    }

    private String getFileNameFromUri(Context context, Uri uri) {
        String result = "image.jpg";
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result;
    }
}
