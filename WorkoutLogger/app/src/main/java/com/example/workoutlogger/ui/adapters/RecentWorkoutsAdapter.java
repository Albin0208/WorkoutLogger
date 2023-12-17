package com.example.workoutlogger.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying a list of recent workouts
 */
public class RecentWorkoutsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_LAST = 1;
    private List<Workout> workouts = new ArrayList<>();

    private final View.OnClickListener onButtonClick;
    private final WorkoutClickListener onWorkoutClick;
    private boolean shouldDisplayLastItem;

    /**
     * Constructor for the adapter that creates a instance where the last item differs from the rest of the items
     *
     * @param onButtonClick The click listener for the last item
     * @param onWorkoutClick The click listener for the rest of the items
     */
    public RecentWorkoutsAdapter(View.OnClickListener onButtonClick, WorkoutClickListener onWorkoutClick) {
        super();
        this.onButtonClick = onButtonClick;
        this.onWorkoutClick = onWorkoutClick;
        this.shouldDisplayLastItem = true;
    }

    /**
     * Constructor for the adapter that creates a instance where the last item differs from the rest of the items
     *
     * @param onButtonClick The click listener for the last item
     * @param onWorkoutClick The click listener for the rest of the items
     * @param shouldDisplayLastItem Whether or not the last item should be displayed
     */
    public RecentWorkoutsAdapter(View.OnClickListener onButtonClick, WorkoutClickListener onWorkoutClick, boolean shouldDisplayLastItem) {
        this(onButtonClick, onWorkoutClick);
        this.shouldDisplayLastItem = shouldDisplayLastItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (shouldDisplayLastItem && viewType == VIEW_TYPE_LAST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item_last, parent, false);
            return new LastItemViewHolder(view, onButtonClick);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new RecentWorkoutsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LastItemViewHolder) {
            return;
        }
        Workout workout = workouts.get(position);
        RecentWorkoutsViewHolder workoutsViewHolder = (RecentWorkoutsViewHolder) holder;
        workoutsViewHolder.bind(workout);
        workoutsViewHolder.itemView.setOnClickListener(v -> onWorkoutClick.WorkoutClicked(workout));
    }

    @Override
    public int getItemViewType(int position) {
        if (!shouldDisplayLastItem)
            return VIEW_TYPE_NORMAL;

        return position == getItemCount() - 1 ? VIEW_TYPE_LAST : VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (!shouldDisplayLastItem)
            return workouts.size();

        return workouts.size() + 1; // Special case for the last item
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    /**
     * View holder for a normal item in the list
     */
    public static class RecentWorkoutsViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutName;
        private final TextView workoutDate;
        private final TextView exercises; // List of all exercises but as a string

        public RecentWorkoutsViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_name);
            workoutDate = itemView.findViewById(R.id.workout_date);
            exercises = itemView.findViewById(R.id.exercise_list);
        }


        public void bind(Workout workout) {
            workoutName.setText(workout.getName());

            SimpleDateFormat formatter = new SimpleDateFormat(
                    itemView.getContext().getString(R.string.date_format), Locale.getDefault());
            workoutDate.setText(formatter.format(workout.getDate().toDate()));

            // Set the text of the exercises to be a comma separated list of all the exercises
            exercises.setText(String.join(",", workout.getExercises().stream()
                    .map(Exercise::getName)
                    .toArray(String[]::new)));
        }
    }

    /**
     * View holder for the last item in the list
     * This item is special since it will be used to navigate to the view all workouts screen
     */
    public static class LastItemViewHolder extends RecyclerView.ViewHolder {


        public LastItemViewHolder(@NonNull View itemView, View.OnClickListener onButtonClick) {
            super(itemView);
            Button button = itemView.findViewById(R.id.full_log_button);

            button.setOnClickListener(onButtonClick);
        }
    }
}
