package com.ranjit.gatepass.sheets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.interfaces.GatePassApiService;
import com.ranjit.gatepass.models.GatePassModel;
import com.ranjit.gatepass.models.GatePassRequest;
import com.ranjit.gatepass.services.ApiClient;
import com.ranjit.gatepass.services.ApiService;
import com.ranjit.gatepass.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGatePass extends BottomSheetDialogFragment {
    private ProgressBar progressBar;
    private Button btnSubmit;
    private EditText detailNotes, etStart, etEnd;
    private Spinner spinnerPassType;

    private String fromDateTimeISO = "";
    private String toDateTimeISO = "";

    public interface GatePassSubmitListener {
        void onSubmit(GatePassModel item);
    }

    private GatePassSubmitListener listener;

    public void setGrocerySubmitListener(GatePassSubmitListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_new_gate_pass, container, false);

        detailNotes = view.findViewById(R.id.et_detail_note);
        spinnerPassType = view.findViewById(R.id.spinner_pass_type);
        btnSubmit = view.findViewById(R.id.btn_submit);
        progressBar = view.findViewById(R.id.progress_bar);
        etStart = view.findViewById(R.id.et_start_datetime);
        etEnd = view.findViewById(R.id.et_end_datetime);

        etStart.setOnClickListener(v -> showMaterialDateTimePicker(etStart, true));
        etEnd.setOnClickListener(v -> showMaterialDateTimePicker(etEnd, false));

        btnSubmit.setOnClickListener(v -> {
            String detailNote = detailNotes.getText().toString();
            String gatePassType = spinnerPassType.getSelectedItem().toString();

            if (detailNote.isEmpty() || fromDateTimeISO.isEmpty() || toDateTimeISO.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);
            int userId = prefs.getInt("userId",0);

            GatePassRequest request = new GatePassRequest(
                    gatePassType,
                    detailNote,
                    fromDateTimeISO,
                    toDateTimeISO,
                    userId
            );

            submitGatePass(request);
        });

        return view;
    }

    private void submitGatePass(GatePassRequest request) {
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("GatePassApp", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "-");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.applyGatePass(request, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Gate pass applied successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to apply pass", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMaterialDateTimePicker(EditText editText, boolean isStart) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build();

        datePicker.show(getChildFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(9)
                    .setMinute(0)
                    .setTitleText("Select Time")
                    .build();

            timePicker.show(getChildFragmentManager(), "TIME_PICKER");

            timePicker.addOnPositiveButtonClickListener(view -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                editText.setText(displayFormat.format(calendar.getTime()));

                if (isStart) {
                    fromDateTimeISO = isoFormat.format(calendar.getTime());
                } else {
                    toDateTimeISO = isoFormat.format(calendar.getTime());
                }
            });
        });
    }
}
