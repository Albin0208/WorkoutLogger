package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.ui.activities.WorkoutActivity;
import com.example.workoutlogger.ui.adapters.RecentWorkoutsAdapter;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
    private WorkoutViewModel workoutViewModel;
    private ProgressBar loadingSpinner;
    private TextView workoutsError;
    private RecentWorkoutsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button startWorkoutButton = view.findViewById(R.id.btn_start_workout);
        RecyclerView recyclerView = view.findViewById(R.id.rv_recent_workouts);
        TextView noWorkoutsText = view.findViewById(R.id.no_workouts);
        workoutsError = view.findViewById(R.id.error_workouts);
        loadingSpinner = view.findViewById(R.id.loading_spinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp){
                lp.width = (int) (getWidth() / 1.5);
                return true;
            }
        });

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        adapter = new RecentWorkoutsAdapter();
        recyclerView.setAdapter(adapter);

        workoutViewModel.getWorkouts();
        workoutViewModel.getWorkoutsLiveData().observe(getViewLifecycleOwner(), this::handleWorkoutsResult);

        workoutViewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> handleVisibility(isEmpty, noWorkoutsText));
        workoutViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> handleVisibility(isLoading, loadingSpinner));

        startWorkoutButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), WorkoutActivity.class)));

        return view;
    }

    /**
     * Handles the result of the workouts
     *
     * @param result The result of the workouts
     */
    private void handleWorkoutsResult(Result<List<Workout>> result) {
        if (result.isSuccess()) {
            adapter.setWorkouts(result.getData());
        } else {
            // Show error to user
            workoutsError.setVisibility(View.VISIBLE);
            workoutsError.setText(result.getError().getMessage());
        }
    }

    /**
     * Handles the visibility of the given views
     *
     * @param isVisible Whether the views should be visible
     * @param views     The views to handle
     */
    private void handleVisibility(boolean isVisible, View... views) {
        for (View v : views)
            v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the recent workouts
        workoutViewModel.getWorkouts();
    }
}