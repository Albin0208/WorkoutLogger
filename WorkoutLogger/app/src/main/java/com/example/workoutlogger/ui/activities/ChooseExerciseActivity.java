package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.ui.fragments.ExerciseListFragment;

public class ChooseExerciseActivity extends AppCompatActivity implements ExerciseOnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ExerciseListFragment(this))
                .commit();
    }

    @Override
    public void onClick(Exercise exercise) {
        Intent intent = new Intent();
        intent.putExtra("exercise", exercise);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

}