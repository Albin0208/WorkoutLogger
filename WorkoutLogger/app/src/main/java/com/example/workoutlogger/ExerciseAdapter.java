package com.example.workoutlogger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterViewHolder> {
    private List<String> exercises; // Todo Create an exercise class to use instead

    public ExerciseAdapter(List<String> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseAdapterViewHolder holder, int position) {
        holder.name.setText(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ExerciseAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
        }
    }
}
