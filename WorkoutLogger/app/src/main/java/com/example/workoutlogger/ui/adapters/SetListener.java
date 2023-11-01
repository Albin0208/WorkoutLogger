package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.ExerciseSet;

public interface SetListener {
    void onSetRemoved(ExerciseSet set, int position, SetAdapter adapter);

    void onSetAdded(ExerciseSet set, int position, SetAdapter adapter);

    void onSetToggleCompletion(ExerciseSet set, int position, SetAdapter adapter);
}
