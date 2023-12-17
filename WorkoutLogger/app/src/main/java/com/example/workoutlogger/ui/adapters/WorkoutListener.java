package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.Exercise;

/**
 * Interface for handling interactions with exercise items in a {@link WorkoutAdapter}
 */
public interface WorkoutListener {
    /**
     * Called when an exercise is removed from the adapter
     *
     * @param exercise the exercise being removed
     * @param position the position of the exercise in the adapter
     */
    void onExerciseRemoved(Exercise exercise, int position);
}
