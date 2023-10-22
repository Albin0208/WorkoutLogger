package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.workoutlogger.R;
import com.example.workoutlogger.ui.fragments.ExerciseListFragment;

public class ChooseExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ExerciseListFragment())
                .commit();
    }

}