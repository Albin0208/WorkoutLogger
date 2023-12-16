package com.example.workoutlogger.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.ui.adapters.SetAdapter;
import com.example.workoutlogger.ui.adapters.SetListener;
import com.example.workoutlogger.ui.adapters.WorkoutListener;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WorkoutActivity extends AppCompatActivity {
    private WorkoutViewModel workoutViewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        setupCustomActionBar();
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        setupOnBackPressedCallback();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        navController = navHostFragment.getNavController();
    }

    /**
     * Sets up the custom action bar
     */
    private void setupCustomActionBar() {
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.workout_toolbar);
        }
    }

    /**
     * Sets up the onBackPressed callback
     */
    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navController.getCurrentDestination().getId() == R.id.workoutFragment) {
                    showAbortWorkout();
                } else {
                    navController.navigateUp();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Shows a dialog asking the user if they want to abort the workout
     */
    private void showAbortWorkout() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_abort_workout, viewGroup, false);

        Button yesButton = customLayout.findViewById(R.id.confirm_button);
        Button noButton = customLayout.findViewById(R.id.cancel_button);

        builder.setView(customLayout);
        final AlertDialog dialog = builder.create();

        yesButton.setOnClickListener(v -> finish());
        noButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController.navigateUp();
        return true;
    }
}
