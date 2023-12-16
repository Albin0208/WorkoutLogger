package com.example.workoutlogger.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.viewmodels.LogViewModel;

public class WorkoutDetailFragment extends Fragment {


    public WorkoutDetailFragment() {
        // Required empty public constructor
    }

    private LogViewModel logViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logViewModel = new ViewModelProvider(requireActivity()).get(LogViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        Workout workout = logViewModel.getWorkout().getValue();

        requireActivity().setTitle(workout.getName());

        return view;
    }
}