package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.workoutlogger.R;

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

        ImageButton continueWorkingOutButton = barView.findViewById(R.id.back_to_workout);
        continueWorkingOutButton.setOnClickListener(v -> finish());

        ImageButton finishWorkoutButton = barView.findViewById(R.id.finish_workout);
        finishWorkoutButton.setOnClickListener(v -> {
            // TODO Save the workout to the database

            // Set the result to RESULT_OK and that the workout is finished
            setResult(RESULT_OK, getIntent().putExtra("finishedWorkout", true));
            finish();
        });
    }
}