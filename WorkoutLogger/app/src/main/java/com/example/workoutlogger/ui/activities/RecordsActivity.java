package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.ExerciseAdapter;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.viewmodels.ExerciseViewModel;

public class RecordsActivity extends AppCompatActivity {
    private ExerciseViewModel exerciseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        // TODO Handle the nav back to main activity so the user can choose a different workout

        // Grab the exercise from the intent
        Exercise exercise = getIntent().getParcelableExtra("exercise", Exercise.class);
        getSupportActionBar().setTitle(exercise.getName());

        // Grab the records for this exercise
        exerciseViewModel.getRecords(exercise).observe(this, records -> {
            // TODO Display the records in a RecyclerView
            Log.d("RecordsActivity", "Records: " + records);
        });

//        NavController navController = Navigation.findNavController(this, R.id.host_fragment);

        // Set the title of the action bar to the name of the exercise


    }
}