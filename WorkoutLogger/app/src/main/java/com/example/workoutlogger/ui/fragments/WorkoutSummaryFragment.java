package com.example.workoutlogger.ui.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutSummaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkoutSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutSummaryFragment newInstance(String param1, String param2) {
        WorkoutSummaryFragment fragment = new WorkoutSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private WorkoutViewModel workoutViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
            barView.findViewById(R.id.finish_workout).setOnClickListener(v -> {
                workoutViewModel.saveWorkout(workoutViewModel.getWorkout());
                requireActivity().finish();
            });

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
                var workout = workoutViewModel.getWorkout();
                workout.setName(workoutName.getText().toString());

                workoutViewModel.saveWorkout(workout);
            });
        }


        workoutViewModel.getWorkoutCreatedResult().observe(requireActivity(), result -> {
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