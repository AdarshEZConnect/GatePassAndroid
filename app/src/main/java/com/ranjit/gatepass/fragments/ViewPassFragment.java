package com.ranjit.gatepass.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.transition.MaterialSharedAxis;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.models.GatePassModel;

public class ViewPassFragment extends Fragment {
    private static final String ARG_NOTE = "note";
    private static final String ARG_REASON = "reason";
    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";
    private static final String ARG_STATUS = "status";
    public static ViewPassFragment newInstance(GatePassModel model) {
        ViewPassFragment fragment = new ViewPassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE, model.getNote());
        args.putString(ARG_REASON, model.getReason());
        args.putString(ARG_START_DATE, model.getFromDate());
        args.putString(ARG_END_DATE, model.getToDate());
        args.putString(ARG_STATUS, model.getStatus());
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Enable back arrow in toolbar

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pass, container, false);
        TextView tvNote = view.findViewById(R.id.tvNoteDetail);
        TextView tvReason = view.findViewById(R.id.tvReason);
        TextView tvDateStart = view.findViewById(R.id.tvDateStart);
        TextView tvDateEnd = view.findViewById(R.id.tvDateEnd);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            tvNote.setText(getArguments().getString(ARG_NOTE));
            tvDateStart.setText(getArguments().getString(ARG_START_DATE));
            tvDateEnd.setText(getArguments().getString(ARG_END_DATE));
            tvReason.setText(getArguments().getString(ARG_REASON));
            tvStatus.setText(getArguments().getString(ARG_STATUS));
        }
        return view;
    }
}