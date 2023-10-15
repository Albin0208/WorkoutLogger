package com.example.workoutlogger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterViewHolder> {
    private List<Exercise> exercises; // Todo Create an exercise class to use instead

    public ExerciseAdapter(List<Exercise> exercises) {
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
        Exercise exercise = exercises.get(position);
        holder.name.setText(exercise.getName());
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
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
