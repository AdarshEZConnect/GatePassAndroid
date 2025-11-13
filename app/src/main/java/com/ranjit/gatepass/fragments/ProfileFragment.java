package com.ranjit.gatepass.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.activity.LoginActivity;

public class ProfileFragment extends Fragment {

    TextView tvName, tvEmail, tvRole, tvDepartment, tvGender, tvParentMobile,tvUserMobile;
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRole = view.findViewById(R.id.tvRole);
        tvDepartment = view.findViewById(R.id.tvDepartment);
        tvGender = view.findViewById(R.id.tvGender);
        tvUserMobile = view.findViewById(R.id.tvUserMobile);
        tvParentMobile = view.findViewById(R.id.tvParentMobile);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        prefs = getActivity().getSharedPreferences("GatePassApp", MODE_PRIVATE);
        String name = prefs.getString("name", "User");
        String email = prefs.getString("email", "-");
        String role = prefs.getString("role", "-");
        String department = prefs.getString("departmentName", "-");
        String gender = prefs.getString("gender", "-");
        String parentMobile = prefs.getString("parentMobile", "-");
        String userMobile = prefs.getString("userMobile", "-");

        // ✅ Set to UI
        tvName.setText(name);
        tvEmail.setText(email);
        tvRole.setText(role);
        tvDepartment.setText(department);
        tvGender.setText(gender);
        tvUserMobile.setText(userMobile);
        tvParentMobile.setText(userMobile);

        toolbar.setNavigationOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Handle logout here
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getContext().startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        ShapeableImageView profileImage = view.findViewById(R.id.profile_image); // set proper ID
        String imageUrl = prefs.getString("profileImage", "-");

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.man)
                .into(profileImage);

        return view;
    }
}