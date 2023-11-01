package com.example.workoutlogger.ui.adapters;

import com.example.workoutlogger.data.ExerciseSet;

/**
 * Interface for handling interactions with exercise set items in a {@link SetAdapter}
 */
public interface SetListener {
    /**
     * Called when a set is removed from the adapter
     *
     * @param set      the set being removed
     * @param adapterPosition the position of the set in the adapter
     * @param adapter  the adapter that the set is removed from
     */
    void onSetRemoved(ExerciseSet set, int adapterPosition, SetAdapter adapter);

    /**
     * Called when a set is added to the adapter
     *
     * @param set      the set being added
     * @param adapterPosition the position of the set in the adapter
     * @param adapter  the adapter that the set is added to
     */
    void onSetAdded(int adapterPosition, SetAdapter adapter);

    /**
     * Called when the completion status of an exercise set is toggled.
     *
     * @param set      the set being toggled
     * @param position the position of the set in the adapter
     * @param adapter  the adapter that the set is in
     */
    void onSetToggleCompletion(ExerciseSet set, int position, SetAdapter adapter);
}
