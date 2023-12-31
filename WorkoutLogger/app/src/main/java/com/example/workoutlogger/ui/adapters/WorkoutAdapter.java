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

public class WorkoutAdapter extends ListAdapter<Exercise, WorkoutAdapter.WorkoutViewHolder> {

    private final SetListener setListener;
    private final WorkoutListener workoutListener;

    public WorkoutAdapter(WorkoutListener listener, SetListener setListener) {
        super(new ExerciseDiffCallback());
        this.workoutListener = listener;
        this.setListener = setListener;
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
        holder.bind(exercise, position);
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final RecyclerView setList;
        private final Button addSetButton;
        private final ImageView menuIcon;
        private final SetAdapter setAdapter;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
            setList = itemView.findViewById(R.id.set_list);
            addSetButton = itemView.findViewById(R.id.add_set_button);
            setAdapter = new SetAdapter(new SetDiffCallback(), setListener);
            setList.setAdapter(setAdapter);
            setList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(itemView.getContext()));
            menuIcon = itemView.findViewById(R.id.menu_icon);
        }

        public void bind(Exercise exercise, int position) {
            name.setText(exercise.getName());
            setAdapter.submitList(exercise.getSets());
            setAdapter.setAdapterPosition(position);

            addSetButton.setOnClickListener(v -> setListener.onSetAdded(position, setAdapter));
            menuIcon.setOnClickListener(v -> showPopupMenu(exercise, position));
        }

        private void showPopupMenu(Exercise exercise, int position) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), menuIcon);
            popupMenu.inflate(R.menu.menu_exercise);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_delete) {
                    workoutListener.onExerciseRemoved(exercise, position);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}
