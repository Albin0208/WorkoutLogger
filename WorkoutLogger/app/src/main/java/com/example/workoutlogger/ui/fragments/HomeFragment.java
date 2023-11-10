package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.ui.activities.WorkoutActivity;
import com.example.workoutlogger.ui.adapters.RecentWorkoutsAdapter;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

public class HomeFragment extends Fragment {
    private WorkoutViewModel workoutViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button startWorkoutButton = view.findViewById(R.id.btn_start_workout);
        RecyclerView recyclerView = view.findViewById(R.id.rv_recent_workouts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp){
                lp.width = (int) (getWidth() / 1.5);
                return true;
            }
        });

        // TODO Add a loading spinner


        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        RecentWorkoutsAdapter adapter = new RecentWorkoutsAdapter();
        recyclerView.setAdapter(adapter);

        workoutViewModel.getWorkouts().observe(getViewLifecycleOwner(), adapter::setWorkouts);

        startWorkoutButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), WorkoutActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the recent workouts
        workoutViewModel.refreshWorkouts();

    }
}