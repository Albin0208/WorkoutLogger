package com.example.workoutlogger.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.workoutlogger.data.Exercise;

public class ExerciseDiffCallback extends DiffUtil.ItemCallback<Exercise> {
    @Override
    public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.getId().equals(newItem.getId()) && oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.equals(newItem);
    }
}
