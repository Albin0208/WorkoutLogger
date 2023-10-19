package com.example.workoutlogger.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

    private FirebaseFirestore db;
    private EditText exerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        getSupportActionBar().setTitle("Create Exercise");

        db = FirebaseFirestore.getInstance();

        exerciseName = findViewById(R.id.new_exercise_name);
        Button saveButton = findViewById(R.id.save_exercise_button);

        saveButton.setOnClickListener(view -> {
            String name = exerciseName.getText().toString();

            if (isValidName(name)) {
                // TODO This should be changed to the users exercises
                ExerciseViewModel viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

                OnCompleteListener<DocumentReference> onCompleteListener = task -> {
                    if (task.isSuccessful()) {
                        // Notify the user that the exercise was created
                        Toast.makeText(CreateExerciseActivity.this, "Exercise Created", Toast.LENGTH_SHORT).show();

                        // Close the activity and go back to the previous screen
                        finish();
                    } else {
                        // Log error message
                        Log.e("CreateExerciseActivity", "Exercise Creation failed", task.getException());
                    }
                };

                viewModel.createExercise(name, onCompleteListener);
            } else {
                // Show an error message
                exerciseName.setError("Please enter a exercise name");
            }
        });

        exerciseName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                exerciseName.setError(null); // Clear the error when the field is focused.
            }
        });

    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.trim().length() > 0;
    }

    private void onExerciseCreationSuccess(DocumentReference ref) {
        String name = exerciseName.getText().toString().trim();

        Exercise exercise = new Exercise(name);

        // Get a reference to your ExerciseViewModel
//        ExerciseViewModel exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
//
//        // Add the newly created exercise to the ViewModel
//        List<Exercise> exercises = new ArrayList<>(exerciseViewModel.getExercises().getValue());
//        exercises.add(exercise);
//        exerciseViewModel.setExercises(exercises);

        Toast.makeText(CreateExerciseActivity.this, "Exercise Created", Toast.LENGTH_SHORT).show();

        finish(); // Close the activity
    }



    private void onExerciseCreationFailure(Exception e) {
        Log.e("CreateExerciseActivity", "Exercise Creation failed", e);
        Toast.makeText(this, "Exercise Creation failed", Toast.LENGTH_SHORT).show();
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