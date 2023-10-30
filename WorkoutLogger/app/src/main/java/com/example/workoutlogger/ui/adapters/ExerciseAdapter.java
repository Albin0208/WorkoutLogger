package com.example.workoutlogger.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder> {
    private final ExerciseOnClickListener listener;

    public ExerciseAdapter() {
        super(new ExerciseDiffCallback());
        this.listener = null;
    }

    public ExerciseAdapter(ExerciseOnClickListener listener) {
        super(new ExerciseDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = getItem(position);
        holder.bind(exercise);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(exercise);
            }
        });

    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
        }

        public void bind(Exercise exercise) {
            name.setText(exercise.getName());
        }
    }
}
