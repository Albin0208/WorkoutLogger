package com.example.workoutlogger.ui.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends ListAdapter<Exercise, WorkoutAdapter.WorkoutViewHolder> {
    public WorkoutAdapter() {
        super(new ExerciseDiffCallback());
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Exercise exercise = getItem(position);
        holder.bind(exercise);

//        holder.addSetButton.setOnClickListener(v -> {
//            int id = holder.sets.size() + 1;
//            int reps = 0;
//            int weight = 0;
//            ExerciseSet exerciseSet = new ExerciseSet(id, reps, weight);
//            holder.sets.add(exerciseSet);
//            holder.setAdapter.submitList(new ArrayList<>(holder.sets));
//        });
    }

    public void addExercise(Exercise exercise) {
        List<Exercise> exercises = new ArrayList<>(getCurrentList());
        exercises.add(exercise);
        submitList(exercises);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final RecyclerView setList;
        private final Button addSetButton;
        SetAdapter setAdapter;
        List<ExerciseSet> sets;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
            setList = itemView.findViewById(R.id.set_list);
            addSetButton = itemView.findViewById(R.id.add_set_button);
            setAdapter = new SetAdapter(new SetDiffCallback());
            setList.setAdapter(setAdapter);
            setList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(itemView.getContext()));
            sets = new ArrayList<>();

            addSetButton.setOnClickListener(v -> {
                int id = sets.size() + 1;
                int reps = 0;
                int weight = 0;
                ExerciseSet exerciseSet = new ExerciseSet(id, reps, weight);
                sets.add(exerciseSet);
                setAdapter.submitList(new ArrayList<>(sets));
            });
        }

        public void bind(Exercise exercise) {
            name.setText(exercise.getName());
        }
    }
}
