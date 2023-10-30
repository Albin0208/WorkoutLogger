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
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends ListAdapter<Exercise, WorkoutAdapter.WorkoutViewHolder> {
    public WorkoutAdapter(List<Exercise> exercises) {
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

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuIcon);
        popupMenu.inflate(R.menu.menu_exercise);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                holder.removeItem(position);
                return true;
            }
            return false;
        });

        holder.menuIcon.setOnClickListener(v -> popupMenu.show());

    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final RecyclerView setList;
        private final Button addSetButton;
        private final ImageView menuIcon;
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
            menuIcon = itemView.findViewById(R.id.menu_icon);

            addSetButton.setOnClickListener(v -> {
//                ExerciseSet exerciseSet = new ExerciseSet(sets.size() + 1, 0, 0, sets.size() + 1);
                // Add the set to the exercise


                int id = setAdapter.getItemCount() + 1;
                int reps = 0;
                int weight = 0;
                ExerciseSet exerciseSet = new ExerciseSet(id, reps, weight, id);
                sets.add(exerciseSet);
                List<ExerciseSet> s = new ArrayList<>(setAdapter.getCurrentList());
                s.add(exerciseSet);
                setAdapter.submitList(new ArrayList<>(s));
            });
        }

        public void bind(Exercise exercise) {
            name.setText(exercise.getName());
        }

        public void removeItem(int position) {
            List<Exercise> exercises = new ArrayList<>(getCurrentList());
            exercises.remove(position);
            submitList(new ArrayList<>(exercises));
        }
    }
}
