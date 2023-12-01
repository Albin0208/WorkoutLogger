package com.example.workoutlogger.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Record;
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
//        getSupportActionBar().setTitle(exercise.getName());
        // Check if the exercise is a usercreated exercise

        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            if (exercise.isUserCreated()) {
                bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                bar.setCustomView(R.layout.exercise_toolbar);
                TextView toolbarTitle = bar.getCustomView().findViewById(R.id.action_bar_title);
                toolbarTitle.setText(exercise.getName());
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowHomeEnabled(true);
            } else
                bar.setTitle(exercise.getName());
        }

        RecordAdapter recordAdapter = new RecordAdapter();
        RecyclerView recyclerView = findViewById(R.id.records_recycler_view);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Grab the records for this exercise
        exerciseViewModel.getRecords(exercise).observe(this, records -> {
            findViewById(R.id.spinner).setVisibility(RecyclerView.GONE);
            List<Record> recordList = records.getData();
            // Check if the exercise has records
            if (recordList.isEmpty()) {
                recyclerView.setVisibility(RecyclerView.GONE);
                findViewById(R.id.no_records_text).setVisibility(RecyclerView.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.no_records_text).setVisibility(View.GONE);

                recordAdapter.setRecords(recordList);
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