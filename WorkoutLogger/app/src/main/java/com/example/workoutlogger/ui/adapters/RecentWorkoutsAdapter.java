package com.example.workoutlogger.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RecentWorkoutsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_LAST = 1;
    private List<Workout> workouts = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LAST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item_last, parent, false);
            return new LastItemViewHolder(view);
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
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? VIEW_TYPE_LAST : VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return workouts.size() + 1; // Special case for the last item
    }

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
        public LastItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
