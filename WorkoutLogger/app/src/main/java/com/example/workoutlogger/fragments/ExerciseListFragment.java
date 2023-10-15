package com.example.workoutlogger.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.workoutlogger.ExerciseViewModel;
import com.example.workoutlogger.activities.CreateExerciseActivity;
import com.example.workoutlogger.adapters.ExerciseAdapter;
import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListFragment extends Fragment {

    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exercises = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        // Grab the exercises from the shared view model
        ExerciseViewModel exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        exerciseViewModel.getExercises().observe(getViewLifecycleOwner(), this::setExercises);

        RecyclerView recyclerView = view.findViewById(R.id.exercise_recyclerView);
        exerciseAdapter = new ExerciseAdapter(exercises);
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
        this.exercises = exercises;
        if (exerciseAdapter != null) {
            exerciseAdapter.setExercises(exercises);
            exerciseAdapter.notifyDataSetChanged();
        }
    }
}
