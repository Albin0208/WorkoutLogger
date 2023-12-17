package com.example.workoutlogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.ui.adapters.RecentWorkoutsAdapter;
import com.example.workoutlogger.ui.adapters.WorkoutClickListener;
import com.example.workoutlogger.viewmodels.LogViewModel;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogViewFragment extends Fragment implements WorkoutClickListener {
    private WorkoutViewModel workoutViewModel;
    private LogViewModel logViewModel;
    private NavController navController;
    private Button dateFromButton;
    private Button dateToButton;

    public LogViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        logViewModel = new ViewModelProvider(requireActivity()).get(LogViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_log_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.log_recycler_view);
        dateFromButton = view.findViewById(R.id.from_date_button);
        dateToButton = view.findViewById(R.id.to_date_button);

        dateFromButton.setOnClickListener(v -> workoutViewModel.getDatePickerDialog(true, requireContext()).show());

        dateToButton.setOnClickListener(v -> workoutViewModel.getDatePickerDialog(false, requireContext()).show());

        RecentWorkoutsAdapter workoutAdapter = new RecentWorkoutsAdapter(null, this, false);
        recyclerView.setAdapter(workoutAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        navController = NavHostFragment.findNavController(this);

        workoutViewModel.getAllWorkouts();

        workoutViewModel.getWorkoutsLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                workoutAdapter.setWorkouts(result.getData());
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat(
                requireContext().getString(R.string.date_format), Locale.getDefault());

        workoutViewModel.getFromDate().observe(getViewLifecycleOwner(), date -> {
            dateFromButton.setText(formatter.format(date));
        });
        workoutViewModel.getToDate().observe(getViewLifecycleOwner(), date -> {
            dateToButton.setText(formatter.format(date));
        });


        return view;
    }

    @Override
    public void WorkoutClicked(Workout workout) {
        logViewModel.setWorkout(workout);
        navController.navigate(R.id.action_logViewFragment_to_workoutDetailFragment);
    }
}