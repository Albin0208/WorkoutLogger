package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.workoutlogger.R;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ImageButton abortWorkout = findViewById(R.id.abort_workout);
        ImageButton finishWorkout = findViewById(R.id.finish_workout);

        abortWorkout.setOnClickListener(v -> {
            // TODO Add a popup to confirm aborting the workout


            finish();
        });

        finishWorkout.setOnClickListener(v -> {
            // TODO Implement finishing the workout
        });
    }
}