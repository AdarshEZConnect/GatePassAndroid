package com.ranjit.gatepass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ranjit.gatepass.R;
import com.ranjit.gatepass.adapters.GatePassAdapter;
import com.ranjit.gatepass.models.GatePassModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.product_recycler_view);
        List<GatePassModel> data = new ArrayList<>();

        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));
        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));
        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));
        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));
        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));
        data.add(new GatePassModel("Sick Leave","24/10/2003","24/10/2003","Pending","Planed 7 days trip with family Planed 7 days trip with family Planed 7 days trip with family ","24/10/2003"));

        GatePassAdapter adapter = new GatePassAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}