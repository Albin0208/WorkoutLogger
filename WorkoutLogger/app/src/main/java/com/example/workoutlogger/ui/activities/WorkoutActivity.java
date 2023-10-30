package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.WorkoutAdapter;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {
    private WorkoutAdapter workoutAdapter;
    private WorkoutViewModel workoutViewModel;

    ActivityResultLauncher<Intent> chooseExerciseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Exercise exercise = result.getData().getParcelableExtra("exercise", Exercise.class);
                    workoutViewModel.addExercise(exercise);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

//        ImageView imageView = findViewById(R.id.menu_icon);
//
////        imageView.setOnClickListener(v -> {
////
////        });

        setupCustomActionBar();
        setupWorkoutViewModel();
        setupRecyclerView();
        setupButtons();
        setupOnBackPressedCallback();
    }

//    private void showMenu() {
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_exercise, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.menu_delete) {
//            showMenu();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * Sets up the custom action bar
     */
    private void setupCustomActionBar() {
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.custom_toolbar);
        }
    }

    /**
     * Sets up the WorkoutViewModel
     */
    private void setupWorkoutViewModel() {
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        workoutViewModel.getExercises().observe(this, exercises -> workoutAdapter.submitList(new ArrayList<>(exercises)));
    }

    /**
     * Sets up the RecyclerView
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.exercise_list);
        workoutAdapter = new WorkoutAdapter(workoutViewModel.getExercises().getValue());
        recyclerView.setAdapter(workoutAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    /**
     *
     */
    private void setupButtons() {
        Button addExerciseButton = findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> chooseExerciseLauncher.launch(new Intent(this, ChooseExerciseActivity.class)));

        ImageButton abortWorkout = findViewById(R.id.abort_workout);
        ImageButton finishWorkout = findViewById(R.id.finish_workout);

        abortWorkout.setOnClickListener(v -> showAbortWorkout());
        finishWorkout.setOnClickListener(v -> handleFinishWorkout());
    }

    /**
     * Sets up the onBackPressed callback
     */
    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showAbortWorkout();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Shows a dialog asking the user if they want to abort the workout
     */
    private void showAbortWorkout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
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

    /**
     * Handles finishing the workout
     */
    private void handleFinishWorkout() {
        // TODO Implement this
    }
}
