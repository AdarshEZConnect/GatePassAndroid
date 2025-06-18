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

import java.util.List;

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
        holder.tvDateRangeStart.setText(model.getFromDate());
        holder.tvDateRangeEnd.setText(model.getToDate());
        holder.tvAppliedDate.setText(model.getAppliedDateTime());
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
            tvDateRangeStart = itemView.findViewById(R.id.tvDateRangeStart);
            tvDateRangeEnd = itemView.findViewById(R.id.tvDateRangeEnd);
        }
    }
}
