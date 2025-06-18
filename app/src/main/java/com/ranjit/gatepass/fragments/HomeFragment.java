package com.ranjit.gatepass.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", 0);

        recyclerView = view.findViewById(R.id.product_recycler_view);
        retryButton = view.findViewById(R.id.btn_retry);

        setupRecyclerView();
        setupFabs(view);
        setupRetryButton();

        loadGatePassData();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new GatePassAdapter(getContext(), passList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupFabs(View view) {
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab_add_grocery);
        ExtendedFloatingActionButton testFab = view.findViewById(R.id.test_fab);

        fab.setOnClickListener(v -> {
            NewGatePass bottomSheet = new NewGatePass();
            bottomSheet.setGrocerySubmitListener(item -> loadGatePassData()); // refresh on submit
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        });

        testFab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SignUpActivity.class));
        });
    }

    private void setupRetryButton() {
        retryButton.setOnClickListener(v -> {
            retryButton.setVisibility(View.GONE);
            loadGatePassData();
        });
    }

    private void loadGatePassData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", MODE_PRIVATE);
        String token = prefs.getString("token", "-");
        if (token == null || token.equals("-")) {
            showRetryButton();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<GatePassResponse>> call = apiService.getGatePasses(userId, "Bearer " + token);

        call.enqueue(new Callback<List<GatePassResponse>>() {
            @Override
            public void onResponse(Call<List<GatePassResponse>> call, Response<List<GatePassResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    passList.clear();
                    passList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    toggleRetryButton(false);
                } else {
                    showRetryButton();
                }
            }

            @Override
            public void onFailure(Call<List<GatePassResponse>> call, Throwable t) {
                showRetryButton();
            }
        });
    }

    private void toggleRetryButton(boolean show) {
        retryButton.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showRetryButton() {
        toggleRetryButton(true);
    }
}
