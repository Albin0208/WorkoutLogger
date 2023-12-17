package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.Workout;

/**
 * Interface for handling clicks on workouts in the workout list in {@link RecentWorkoutsAdapter}
 */
public interface WorkoutClickListener {
    /**
     * Handles clicks on workouts in the workout list in {@link RecentWorkoutsAdapter}
     *
     * @param workout The workout that was clicked
     */
    void WorkoutClicked(Workout workout);
}
