package com.example.workoutlogger.ui.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;

public class WorkoutDetailsAdapter extends ListAdapter<Exercise, WorkoutDetailsAdapter.WorkoutViewHolder> {


    public WorkoutDetailsAdapter() {
        super(new ExerciseDiffCallback());
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_detail_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Exercise exercise = getItem(position);
        holder.bind(exercise, position);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final RecyclerView setList;
        private final SetDetailAdapter setAdapter;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
            setList = itemView.findViewById(R.id.set_list);
            setAdapter = new SetDetailAdapter(new SetDiffCallback());
            setList.setAdapter(setAdapter);
            setList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(itemView.getContext()));
        }

        public void bind(Exercise exercise, int position) {
            name.setText(exercise.getName());
            setAdapter.submitList(exercise.getSets());
        }

    }
}
