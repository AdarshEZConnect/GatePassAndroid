package com.ranjit.gatepass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.ranjit.gatepass.R;
import com.ranjit.gatepass.models.GatePassModel;

import java.util.List;

public class GatePassAdapter extends RecyclerView.Adapter<GatePassAdapter.ViewHolder> {

    private Context context;
    private List<GatePassModel> gatePassList;

    public GatePassAdapter(Context context, List<GatePassModel> gatePassList) {
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
        GatePassModel model = gatePassList.get(position);
        holder.tvStudentName.setText(model.getStudentName());
        holder.tvReason.setText("Reason: " + model.getReason());
        holder.tvDateRange.setText("From: " + model.getFromDate() + " To: " + model.getToDate());
        holder.chipStatus.setText(model.getStatus());

        holder.tvViewDetails.setOnClickListener(v ->
                Toast.makeText(context, "Details for " + model.getStudentName(), Toast.LENGTH_SHORT).show());

        holder.arrowIcon.setOnClickListener(v ->
                Toast.makeText(context, "Arrow clicked", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return gatePassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvReason, tvDateRange, tvViewDetails;
        Chip chipStatus;
        ImageView arrowIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvDateRange = itemView.findViewById(R.id.tvRange);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
        }
    }
}
