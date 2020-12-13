package com.example.tabataapplication.Adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabataapplication.R;

public class SeqViewHolder extends RecyclerView.ViewHolder {
    final TextView seqTitle;
    final LinearLayout seqItemLayout;

    public SeqViewHolder(@NonNull View itemView) {
        super(itemView);
        seqTitle = itemView.findViewById(R.id.seqTitle);
        seqItemLayout = itemView.findViewById(R.id.seqItemLayout);
    }
}
