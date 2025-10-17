package com.ranjit.gatepass.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.activity.SignUpActivity;
import com.ranjit.gatepass.adapters.GatePassAdapter;
import com.ranjit.gatepass.models.GatePassResponse;
import com.ranjit.gatepass.services.ApiClient;
import com.ranjit.gatepass.services.ApiService;
import com.ranjit.gatepass.sheets.NewGatePass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private GatePassAdapter adapter;
    private List<GatePassResponse> passList = new ArrayList<>();
    private FloatingActionButton retryButton;
    private View emptyStateLayout;
    private ProgressBar progressBar;

    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", 0);

        recyclerView = view.findViewById(R.id.product_recycler_view);
        retryButton = view.findViewById(R.id.empty_retry_btn);
        emptyStateLayout = view.findViewById(R.id.empty_state_layout);
        progressBar = view.findViewById(R.id.progressBar);

        // ✅ Initialize RecyclerView + Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GatePassAdapter(getContext(),passList);
        recyclerView.setAdapter(adapter);

        setupRetryButton();
        setupFabs(view);
        loadGatePassData();

        return view;
    }


    private void loadGatePassData() {
        showLoading(true); // show progress bar

        SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", MODE_PRIVATE);
        String token = prefs.getString("token", "-");
        if (token == null || token.equals("-")) {
            showLoading(false);
            showEmptyState();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<GatePassResponse>> call = apiService.getGatePasses(userId, "Bearer " + token);

        call.enqueue(new Callback<List<GatePassResponse>>() {
            @Override
            public void onResponse(Call<List<GatePassResponse>> call, Response<List<GatePassResponse>> response) {
                showLoading(false); // hide progress bar

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    passList.clear();
                    passList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    showRecyclerView();
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<GatePassResponse>> call, Throwable t) {
                showLoading(false);
                showEmptyState();
            }
        });
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }


    private void showRecyclerView() {
        emptyStateLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setupFabs(View view) {
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab_add_pass);
        ExtendedFloatingActionButton testFab = view.findViewById(R.id.test_fab);

        fab.setOnClickListener(v -> {
            NewGatePass bottomSheet = new NewGatePass();
            bottomSheet.setGrocerySubmitListener(item -> loadGatePassData()); // refresh on submit
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        });

        testFab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SignUpActivity.class));
        });

        retryButton.setOnClickListener(v -> {
            showLoading(true);
            loadGatePassData();
        });
    }
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);

        if (emptyStateLayout != null)
            emptyStateLayout.setVisibility(View.GONE);

        if (retryButton != null)
            retryButton.setVisibility(View.GONE);
    }

    private void setupRetryButton() {
        retryButton.setOnClickListener(v -> {
            retryButton.setVisibility(View.GONE);
            showLoading(true);  // 👈 show progress before calling API
            loadGatePassData();
        });
    }

}
