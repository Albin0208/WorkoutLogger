package com.example.workoutlogger.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.ExerciseAdapter;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.viewmodels.ExerciseViewModel;

public class RecordsActivity extends AppCompatActivity implements ExerciseOnClickListener {

    private RecyclerView recyclerView;
    private ExerciseViewModel exerciseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recyclerView = findViewById(R.id.records_recycler_view);

        exerciseViewModel = new ExerciseViewModel();

        ExerciseAdapter adapter = new ExerciseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        exerciseViewModel.getExercises().observe(this, adapter::submitList);
    }

    @Override
    public void onClick(Exercise exercise) {
        // TODO Open a view which show the records for the exercise
    }
}