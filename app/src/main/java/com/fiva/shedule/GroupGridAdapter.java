package com.fiva.shedule;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fiva.shedule.backend.struct.Group;

import java.util.ArrayList;

public class GroupGridAdapter extends RecyclerView.Adapter<GroupGridAdapter.ViewHolder>{

    private ArrayList<Group> groupsList = new ArrayList<>();
    private int selectedPosition = -1;
    private final OnGroupSelectedListener listener;

    interface OnGroupSelectedListener {
        void onGroupSelected(boolean selected);
    }

    public GroupGridAdapter(OnGroupSelectedListener listener) {
        this.listener = listener;
    }

    void updateData(ArrayList<Group> groupsList) {
        this.groupsList = groupsList;
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType")
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group item = groupsList.get(position);
        holder.textView.setText(item.getName());
        holder.textView.setSelected(selectedPosition == position);

        if (holder.textView.isSelected()) {
            holder.textView.setTextColor(Color.WHITE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(view -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            int previousSelected = selectedPosition;

            if (selectedPosition == currentPos) {
                selectedPosition = -1;
                item.setSelected(false);
                notifyItemChanged(currentPos);
            } else {
                if (previousSelected != -1) {
                    groupsList.get(previousSelected).setSelected(false);
                }
                selectedPosition = currentPos;
                item.setSelected(true);

                if (previousSelected != -1) notifyItemChanged(previousSelected);
                if (selectedPosition != -1) notifyItemChanged(selectedPosition);
            }
            if (listener != null) {
                listener.onGroupSelected(selectedPosition != -1);
            }
        });
    }

    public Group getSelectedItem() {
        if (selectedPosition != -1) {
            return groupsList.get(selectedPosition);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemGroup);
        }
    }
}
