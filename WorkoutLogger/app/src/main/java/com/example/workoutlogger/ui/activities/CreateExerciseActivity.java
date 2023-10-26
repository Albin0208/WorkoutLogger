package com.example.workoutlogger.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.workoutlogger.viewmodels.ExerciseViewModel;
import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity {
    private EditText exerciseName;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        getSupportActionBar().setTitle(R.string.create_exercise);

        exerciseName = findViewById(R.id.new_exercise_name);
        spinner = findViewById(R.id.new_exercise_name_loading);
        Button saveButton = findViewById(R.id.save_exercise_button);

        saveButton.setOnClickListener(view -> {
            String name = exerciseName.getText().toString();
            // Display a loading spinner
            spinner.setVisibility(ProgressBar.VISIBLE);

            ExerciseViewModel viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

            viewModel.getExerciseCreatedResult().observe(this, result -> {
                if (result.isSuccess()) {
                    // Notify the user that the exercise was created
                    Toast.makeText(CreateExerciseActivity.this, "Exercise Created", Toast.LENGTH_SHORT).show();

                    // Close the activity and go back to the previous screen
                    finish();
                } else {
                    spinner.setVisibility(ProgressBar.GONE);
                    // Log the error
                    Log.e("CreateExerciseActivity", "Error creating exercise", result.getError());
                    // Notify the user that the exercise was not created
                    Toast.makeText(CreateExerciseActivity.this, R.string.server_error_on_exercise_creation, Toast.LENGTH_SHORT).show();
                }
            });

            if (viewModel.isValidName(name)) {
                viewModel.createUserExercise(name);
            } else {
                spinner.setVisibility(ProgressBar.GONE);
                // Show an error message
                exerciseName.setError(getString(R.string.enter_an_exercise_error));
                exerciseName.requestFocus();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}