package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.adapters.WorkoutAdapter;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Objects;

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

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        View view = getSupportActionBar().getCustomView();

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        // Setup recycler view
        RecyclerView recyclerView = findViewById(R.id.exercise_list);
        workoutAdapter = new WorkoutAdapter();
        recyclerView.setAdapter(workoutAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        workoutViewModel.getExercises().observe(this, exercises -> {
            workoutAdapter.submitList(new ArrayList<>(exercises));
        });

        Button addExerciseButton = findViewById(R.id.add_exercise_button);

        addExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseExerciseActivity.class);
            chooseExerciseLauncher.launch(intent);
        });


        ImageButton abortWorkout = view.findViewById(R.id.abort_workout);
        ImageButton finishWorkout = view.findViewById(R.id.finish_workout);

        abortWorkout.setOnClickListener(v -> showAbortWorkout());

        finishWorkout.setOnClickListener(v -> {
            // TODO Implement finishing the workout
        });
    }

    /**
     * Shows a dialog asking the user if they want to abort the workout
     */
    private void showAbortWorkout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_abort_workout, null);

        Button yesButton = customLayout.findViewById(R.id.confirm_button);
        Button noButton = customLayout.findViewById(R.id.cancel_button);

        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        // Apply the custom background to the dialog's window
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.rounded_dialog_bg); // Reference to your custom background drawable
        }

        yesButton.setOnClickListener(v1 -> finish());

        noButton.setOnClickListener(v1 -> dialog.dismiss());


        dialog.show();
    }
}