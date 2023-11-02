package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

public class FinishWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_workout);

        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle(R.string.finish_workout);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.finish_workout_toolbar);

        }

        assert bar != null;
        View barView = bar.getCustomView();

        WorkoutViewModel viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        viewModel.getWorkoutCreatedResult().observe(this, result -> {
            if (result.isSuccess()) {
                // Set the result to RESULT_OK and that the workout is finished
                setResult(RESULT_OK, getIntent().putExtra("finishedWorkout", true));
                finish();
            }
            else {
                TextView errorText = findViewById(R.id.workout_name_error);
                errorText.setText(result.getError().getMessage());
                errorText.setVisibility(View.VISIBLE);
            }
        });

        Workout workout = getIntent().getParcelableExtra("workout", Workout.class);// Grab the workout

        // Get the name that the user has entered
        EditText workoutName = findViewById(R.id.workout_name);
        ProgressBar spinner = findViewById(R.id.workout_name_loading);

        ImageButton continueWorkingOutButton = barView.findViewById(R.id.back_to_workout);
        continueWorkingOutButton.setOnClickListener(v -> finish());

        ImageButton finishWorkoutButton = barView.findViewById(R.id.finish_workout);
        finishWorkoutButton.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            // Check that the user has entered a name for the workout
            if (workoutName.getText().toString().isEmpty()) {
                spinner.setVisibility(View.GONE);
                workoutName.setError(getString(R.string.workout_name_required));
                workoutName.requestFocus();
                return;
            }

            workout.setName(workoutName.getText().toString());

            viewModel.saveWorkout(workout);
        });
    }
}