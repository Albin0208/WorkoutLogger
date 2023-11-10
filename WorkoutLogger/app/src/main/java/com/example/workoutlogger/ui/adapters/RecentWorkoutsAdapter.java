package com.example.workoutlogger.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Workout;

import java.util.List;
import java.util.stream.Collectors;

public class RecentWorkoutsAdapter extends RecyclerView.Adapter<RecentWorkoutsAdapter.RecentWorkoutsViewHolder>{
    private List<Workout> workouts;

    @NonNull
    @Override
    public RecentWorkoutsAdapter.RecentWorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new RecentWorkoutsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentWorkoutsAdapter.RecentWorkoutsViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.bind(workout);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public static class RecentWorkoutsViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutName;
        private final TextView workoutDate;
        private final TextView exercises; // List of all exercises but as a string

        public RecentWorkoutsViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_name);
            workoutDate = itemView.findViewById(R.id.workout_date);
            exercises = itemView.findViewById(R.id.exercise_list);
        }


        public void bind(Workout workout) {
            workoutName.setText(workout.getName());
//            workoutDate.setText(workout.getDate().toString()); // TODO Add a actual date to the workout
            workoutDate.setText("2023-11-10");

            // Set the text of the exercises to be a comma separated list of all the exercises
            exercises.setText(
                workout.getExercises().stream()
                    .map(Exercise::getName)
                    .collect(Collectors.joining(", "))
            );

        }
    }
}
