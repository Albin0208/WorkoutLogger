package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.workoutlogger.R;
import com.example.workoutlogger.ui.activities.WorkoutActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button startWorkoutButton = view.findViewById(R.id.btn_start_workout);

        startWorkoutButton.setOnClickListener(v -> {
            // Start new workout activity
            Intent intent = new Intent(getActivity(), WorkoutActivity.class); // Replace 'WorkoutActivity' with the name of your activity
            startActivity(intent);
        });

        return view;
    }
}