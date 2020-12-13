package com.example.tabataapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabataapplication.Models.Phase;
import com.example.tabataapplication.R;

import java.util.List;

public class PhaseDataAdapter extends RecyclerView.Adapter<PhaseViewHolder> {
    private final LayoutInflater inflater;
    private final List<Phase> phases;

    PhaseDataAdapter(Context context, List<Phase> phases) {
        this.inflater = LayoutInflater.from(context);
        this.phases = phases;
    }

    @NonNull
    @Override
    public PhaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.phase_item, parent, false);
        return new PhaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhaseViewHolder holder, int position) {
        Phase phase = phases.get(position);
        holder.actionDescription.setText(phase.getDescription());
        holder.time.setText(phase.getTime());
        holder.actionName.setText(phase.getActionName().toString());
        holder.actionImage.setImageDrawable(phase.getActionImage());
    }

    @Override
    public int getItemCount() {
        return phases.size();
    }
}
