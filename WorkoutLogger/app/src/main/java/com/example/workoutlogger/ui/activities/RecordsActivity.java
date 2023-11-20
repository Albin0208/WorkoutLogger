package com.example.workoutlogger.ui.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Record;
import com.example.workoutlogger.ui.adapters.ExerciseAdapter;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.ui.adapters.RecordAdapter;
import com.example.workoutlogger.viewmodels.ExerciseViewModel;

import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    private ExerciseViewModel exerciseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        // Grab the exercise from the intent
        Exercise exercise = getIntent().getParcelableExtra("exercise", Exercise.class);
        getSupportActionBar().setTitle(exercise.getName());

        RecordAdapter recordAdapter = new RecordAdapter();
        RecyclerView recyclerView = findViewById(R.id.records_recycler_view);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Grab the records for this exercise
        exerciseViewModel.getRecords(exercise).observe(this, records -> {
            findViewById(R.id.spinner).setVisibility(RecyclerView.GONE);
            // Check if records are not 0
            if (records.getData().size() == 0) {
                recyclerView.setVisibility(RecyclerView.GONE);
                findViewById(R.id.no_records_text).setVisibility(RecyclerView.VISIBLE);
            }


            recordAdapter.setRecords(records.getData());
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