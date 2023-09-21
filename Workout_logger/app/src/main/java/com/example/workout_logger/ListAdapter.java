package com.example.workout_logger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder> {
    private List<String> names;

    public ListAdapter(List<String> names) {
        this.names = names;
    }
    @NonNull
    @Override
    public ListAdapter.ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListAdapterViewHolder holder, int position) {
        holder.name.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ListAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
        }
    }
}
