package com.example.workoutlogger.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.ui.adapters.WorkoutAdapter;
import com.example.workoutlogger.ui.adapters.WorkoutDetailsAdapter;
import com.example.workoutlogger.viewmodels.LogViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WorkoutDetailFragment extends Fragment {


    public WorkoutDetailFragment() {
        // Required empty public constructor
    }

    private LogViewModel logViewModel;
    private TextView workoutNameTextView;
    private TextView workoutDateTextView;

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

        workoutNameTextView = view.findViewById(R.id.workoutTitleTextView);

        logViewModel.getWorkoutName().observe(getViewLifecycleOwner(), name -> {
            if (name != null) {
                workoutNameTextView.setText(name);
            }
        });

        workoutDateTextView = view.findViewById(R.id.workoutDateTextView);

        logViewModel.getWorkoutDate().observe(getViewLifecycleOwner(), date -> {
            if (date != null) {
                // Format date
                SimpleDateFormat formatter = new SimpleDateFormat(
                        getString(R.string.date_format) + " - " + getString(R.string.time_format), Locale.getDefault());
                workoutDateTextView.setText(formatter.format(date.toDate()));
            }
        });

        WorkoutDetailsAdapter workoutDetailsAdapter = new WorkoutDetailsAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.exerciseRecyclerView);
        recyclerView.setAdapter(workoutDetailsAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

        logViewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null) {
                workoutDetailsAdapter.setExercises(exercises);
            }
        });


        return view;
    }
}