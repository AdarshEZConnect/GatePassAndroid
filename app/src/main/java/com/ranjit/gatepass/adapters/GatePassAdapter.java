package com.ranjit.gatepass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.fragments.ViewPassFragment;
import com.ranjit.gatepass.models.GatePassModel;
import com.ranjit.gatepass.models.GatePassResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GatePassAdapter extends RecyclerView.Adapter<GatePassAdapter.ViewHolder> {

    private Context context;
    private List<GatePassResponse> gatePassList;

    public GatePassAdapter(Context context, List<GatePassResponse> gatePassList) {
        this.context = context;
        this.gatePassList = gatePassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gatepass, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GatePassResponse model = gatePassList.get(position);
        holder.tvReason.setText(model.getPassType());
        holder.tvNote.setText(model.getReason());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat outputWithTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        try {
            String from = model.getFromDate();
            String to = model.getToDate();
            String applied = model.getAppliedDateTime();

            if (from != null && !from.isEmpty()) {
                Date fromDate = inputFormat.parse(from);
                if (fromDate != null)
                    holder.tvDateRangeStart.setText(outputFormat.format(fromDate));
            } else {
                holder.tvDateRangeStart.setText("-");
            }

            if (to != null && !to.isEmpty()) {
                Date toDate = inputFormat.parse(to);
                if (toDate != null)
                    holder.tvDateRangeEnd.setText(outputFormat.format(toDate));
            } else {
                holder.tvDateRangeEnd.setText("-");
            }

            if (applied != null && !applied.isEmpty()) {
                Date appliedDate = inputFormat.parse(applied);
                if (appliedDate != null)
                    holder.tvAppliedDate.setText(outputWithTime.format(appliedDate));
            } else {
                holder.tvAppliedDate.setText("-");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            // fallback in case parsing fails
            holder.tvDateRangeStart.setText(model.getFromDate() != null ? model.getFromDate() : "-");
            holder.tvDateRangeEnd.setText(model.getToDate() != null ? model.getToDate() : "-");
            holder.tvAppliedDate.setText(model.getAppliedDateTime() != null ? model.getAppliedDateTime() : "-");
        }


        holder.chipStatus.setText(model.getStatus());


        holder.cardView.setOnClickListener(v -> {
            ViewPassFragment fragment = ViewPassFragment.newInstance(model);

            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment) // use your actual container ID
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return gatePassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;

        TextView tvDateRangeStart, tvDateRangeEnd, tvReason, tvNote,tvAppliedDate;
        Chip chipStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.myCard);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvNote = itemView.findViewById(R.id.tvNote);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            tvAppliedDate = itemView.findViewById(R.id.tvAppliedDate);
            tvAppliedDate.setVisibility(View.GONE);
            tvDateRangeStart = itemView.findViewById(R.id.tvDateRangeStart);
            tvDateRangeEnd = itemView.findViewById(R.id.tvDateRangeEnd);
        }
    }
}
