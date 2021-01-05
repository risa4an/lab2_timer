package com.example.tabataapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabataapplication.ActionBroadcast;
import com.example.tabataapplication.DatabaseHelper.DatabaseAdapter;
import com.example.tabataapplication.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.tabataapplication.Models.Phase;
import com.example.tabataapplication.R;

import java.util.Collections;
import java.util.List;

public class EditDataAdapter extends RecyclerView.Adapter<EditViewHolder>
        implements ItemTouchHelperAdapter {
    private final LayoutInflater inflater;
    private final List<Phase> phases;
    private final Context context;

    public EditDataAdapter(Context context, List<Phase> phases) {
        this.inflater = LayoutInflater.from(context);
        this.phases = phases;
        this.context = context;
    }

    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.edit_item, parent, false);
        return new EditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditViewHolder holder, int position) {
        final Phase phase = phases.get(position);
        holder.editImage.setImageDrawable(phase.getActionImage());
        holder.editDescription.setText(phase.getDescription());
        holder.editTime.setText(String.valueOf(phase.getTime()));
        holder.editActionName.setText(phase.getActionName());

        holder.fabIncrease.setOnClickListener(v -> {
            phase.setTime(phase.getTime() + 1);
            sendBroadcast(position, ActionBroadcast.CHANGE);
            holder.editTime.setText(String.valueOf(phase.getTime()));
        });
        holder.fabDecrease.setOnClickListener(v -> {
            phase.setTime(phase.getTime() - 1);
            sendBroadcast(position, ActionBroadcast.CHANGE);
            holder.editTime.setText(String.valueOf(phase.getTime()));
        });
    }

    private void sendBroadcast(int position, ActionBroadcast action) {
        Intent intent = new Intent("phase-changed");
        intent.putExtra("phase-position", position);
        intent.putExtra("action", action);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return phases.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(phases, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(phases, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        phases.remove(position);
        sendBroadcast(position, ActionBroadcast.DELETE);
        notifyItemRemoved(position);
    }
}
