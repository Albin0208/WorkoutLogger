package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.Exercise;

/**
 * Interface for handling clicks on exercises in the exercise list
 */
public interface ExerciseOnClickListener {
    /**
     * Handles clicks on exercises in the exercise list
     *
     * @param exercise The exercise that was clicked
     */
    void onClick(Exercise exercise);
}
