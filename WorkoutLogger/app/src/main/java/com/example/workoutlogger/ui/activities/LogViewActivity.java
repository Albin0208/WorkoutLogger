package com.example.workoutlogger.ui.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.viewmodels.LogViewModel;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

public class LogViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        Workout workout = getIntent().getParcelableExtra("workout", Workout.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        NavController navController = navHostFragment.getNavController();

        LogViewModel logViewModel = new ViewModelProvider(this).get(LogViewModel.class);

        if (workout != null) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    finish();
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);

            logViewModel.setWorkout(workout);
            navController.navigate(R.id.action_logViewFragment_to_workoutDetailFragment);
        }

    }
}