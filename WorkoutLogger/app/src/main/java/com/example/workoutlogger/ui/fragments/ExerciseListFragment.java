package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.ui.activities.ChooseExerciseActivity;
import com.example.workoutlogger.ui.activities.CreateExerciseActivity;
import com.example.workoutlogger.ui.adapters.ExerciseAdapter;
import com.example.workoutlogger.ui.adapters.ExerciseOnClickListener;
import com.example.workoutlogger.viewmodels.ExerciseViewModel;

import java.util.List;

public class ExerciseListFragment extends Fragment {

    private ExerciseAdapter exerciseAdapter;
    private ProgressBar loadingSpinner;
    private final ExerciseOnClickListener listener;

    public ExerciseListFragment() {
        // TODO Replace with going to detail view of exercise
        this.listener = exercise -> {
            Toast.makeText(getContext(), "Exercise clicked: " + exercise.getName(), Toast.LENGTH_SHORT).show();
        };
    }

    public ExerciseListFragment(ChooseExerciseActivity chooseExerciseActivity) {
        this.listener = chooseExerciseActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        loadingSpinner = view.findViewById(R.id.loading_spinner);

        // Grab the exercises from the shared view model
        ExerciseViewModel exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        exerciseViewModel.getExercises().observe(getViewLifecycleOwner(), this::setExercises);

        RecyclerView recyclerView = view.findViewById(R.id.exercise_recyclerView);
        exerciseAdapter = new ExerciseAdapter(listener);
        recyclerView.setAdapter(exerciseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button createExerciseButton = view.findViewById(R.id.add_exercise_button);

        createExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateExerciseActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void setExercises(List<Exercise> exercises) {
        Log.d("ExerciseListFragment", "setExercises: " + exercises);
        loadingSpinner.setVisibility(View.GONE);
        if (exerciseAdapter != null) {
            exerciseAdapter.submitList(exercises);
        }
    }
}
