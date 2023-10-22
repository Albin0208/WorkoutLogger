package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.ui.fragments.ExerciseListFragment;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

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
        intent.putExtra("exercise", (Parcelable) exercise);
        setResult(RESULT_OK, intent);
        finish();
    }
}