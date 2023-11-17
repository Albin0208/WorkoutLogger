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
    }
}