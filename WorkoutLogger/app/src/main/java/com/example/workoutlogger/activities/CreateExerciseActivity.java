package com.example.workoutlogger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workoutlogger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        getSupportActionBar().setTitle("Create Exercise");

        db = FirebaseFirestore.getInstance();

        EditText exerciseName = findViewById(R.id.new_exercise_name);
        Button saveButton = findViewById(R.id.save_exercise_button);

        saveButton.setOnClickListener(view -> {
            String name = exerciseName.getText().toString();

            if (!name.isEmpty()) {
                Map<String, Object> exercise = new HashMap<>();
                exercise.put("name", name);

                db.collection("exercises")
                    .add(exercise)
                    .addOnSuccessListener(this::onExerciseCreationSuccess)
                    .addOnFailureListener(this::onExerciseCreationFailure);

            }
        });
    }

    private void onExerciseCreationSuccess(DocumentReference ref) {
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