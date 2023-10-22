package com.example.workoutlogger.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.workoutlogger.data.ExerciseSet;

public class SetDiffCallback extends DiffUtil.ItemCallback<ExerciseSet> {
    @Override
    public boolean areItemsTheSame(@NonNull ExerciseSet oldItem, @NonNull ExerciseSet newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ExerciseSet oldItem, @NonNull ExerciseSet newItem) {
        return oldItem.equals(newItem);
    }
}
