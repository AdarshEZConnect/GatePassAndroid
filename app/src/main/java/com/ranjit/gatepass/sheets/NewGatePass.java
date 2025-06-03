package com.ranjit.gatepass.sheets;

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
import com.ranjit.gatepass.services.RetrofitClientForGatePass;
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
    private EditText detailNotes;
    private Spinner spinnerPassType;

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

        // Initialize the UI components
        detailNotes = view.findViewById(R.id.et_detail_note);
        spinnerPassType = view.findViewById(R.id.spinner_pass_type);



        btnSubmit = view.findViewById(R.id.btn_submit);
        progressBar = view.findViewById(R.id.progress_bar);

        EditText etStart = view.findViewById(R.id.et_start_datetime);
        EditText etEnd = view.findViewById(R.id.et_end_datetime);

        String startDateTime = String.valueOf(etStart.getText());
        String endtDateTime = String.valueOf(etStart.getText());

        etStart.setOnClickListener(v -> showMaterialDateTimePicker(etStart));
        etEnd.setOnClickListener(v -> showMaterialDateTimePicker(etEnd));

        // Set OnClickListener for the Submit button
        btnSubmit.setOnClickListener(v -> {
            String detailNote = detailNotes.getText().toString();
            String gatePassType = spinnerPassType.getSelectedItem().toString();

            // Validate input fields
            if ( detailNote.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String currentDate = DateUtil.getCurrentDate();

            // Create GatePass Item object
            GatePassModel item = new GatePassModel(
                    gatePassType,
                    startDateTime,
                    endtDateTime,
                    "Pending",
                    detailNote,
                    currentDate
            );

            // API call to submit grocery item
            submitGatePass(item);
        });

        return view;
    }
    private void submitGatePass(GatePassModel item) {
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        GatePassApiService apiService = RetrofitClientForGatePass.getGroceryApiService();

        apiService.addItem(item).enqueue(new Callback<GatePassModel>() {
            @Override
            public void onResponse(@NonNull Call<GatePassModel> call, @NonNull Response<GatePassModel> response) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Gate Pass added successfully!", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onSubmit(response.body());
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to add Gate Pass", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(@NonNull Call<GatePassModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showMaterialDateTimePicker(EditText editText) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build();

        datePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText("Select Time")
                    .build();

            timePicker.show(getChildFragmentManager(), "MATERIAL_TIME_PICKER");

            timePicker.addOnPositiveButtonClickListener(view -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String formatted = formatter.format(calendar.getTime());
                editText.setText(formatted);
            });
        });
    }


}
