package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.Exercise;

public interface WorkoutListener {
    void onExerciseRemoved(Exercise exercise, int position);
}
