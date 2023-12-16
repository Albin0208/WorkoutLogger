package com.example.workoutlogger.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

public class WorkoutSummaryFragment extends Fragment {

    public WorkoutSummaryFragment() {
        // Required empty public constructor
    }

    private WorkoutViewModel workoutViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_summary, container, false);

        var bar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        EditText workoutName = view.findViewById(R.id.workout_name);
        ProgressBar spinner = view.findViewById(R.id.workout_name_loading);

        if (bar != null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setCustomView(R.layout.finish_workout_toolbar);

            View barView = bar.getCustomView();

            ImageButton finishWorkoutButton = barView.findViewById(R.id.finish_workout);
            finishWorkoutButton.setOnClickListener(v -> {
                spinner.setVisibility(View.VISIBLE);
                // Check that the user has entered a name for the workout
                if (workoutName.getText().toString().isEmpty()) {
                    spinner.setVisibility(View.GONE);
                    workoutName.setError(getString(R.string.workout_name_required));
                    workoutName.requestFocus();
                    return;
                }

                workoutViewModel.saveWorkout();
            });
        }

        workoutName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                workoutViewModel.setWorkoutName(editable.toString());
            }
        });

        workoutName.setText(workoutViewModel.getWorkoutName().getValue());

        workoutViewModel.getWorkoutCreatedResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                requireActivity().finish();
            }
            else {
                TextView errorText = view.findViewById(R.id.workout_name_error);
                errorText.setText(result.getError().getMessage());
                errorText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}