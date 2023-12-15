package com.example.workoutlogger.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.ui.activities.ChooseExerciseActivity;
import com.example.workoutlogger.ui.adapters.SetAdapter;
import com.example.workoutlogger.ui.adapters.SetListener;
import com.example.workoutlogger.ui.adapters.WorkoutAdapter;
import com.example.workoutlogger.ui.adapters.WorkoutListener;
import com.example.workoutlogger.viewmodels.WorkoutViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment implements WorkoutListener, SetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutFragment newInstance(String param1, String param2) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private WorkoutViewModel workoutViewModel;
    private NavController navController;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        // Change the toolbar to the workout toolbar
        var bar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        navController = NavHostFragment.findNavController(this);
        if (bar != null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.workout_toolbar);

            View customView = bar.getCustomView();
            ImageButton abortWorkout = customView.findViewById(R.id.abort_workout);

            abortWorkout.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

            ImageButton finishWorkout = customView.findViewById(R.id.finish_workout);
            finishWorkout.setOnClickListener(v -> handleFinishWorkout());
        }

        view.findViewById(R.id.add_exercise_button).setOnClickListener(v -> {
            // Launch the ChooseExerciseActivity
            chooseExerciseLauncher.launch(new Intent(getActivity(), ChooseExerciseActivity.class));
        });

        RecyclerView recyclerView = view.findViewById(R.id.exercise_list);

        WorkoutAdapter adapter = new WorkoutAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        workoutViewModel.getExercises().observe(requireActivity(), exercises -> adapter.submitList(new ArrayList<>(exercises)));

        return view;
    }

    private void handleFinishWorkout() {
        if (workoutViewModel.getExercises().getValue() == null || workoutViewModel.getExercises().getValue().isEmpty()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
            builder.setTitle(R.string.cannot_finish_workout);
            builder.setMessage(R.string.add_exercise_before_finish_text);

            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();

            dialog.show();

            return;
        }

        navController.navigate(R.id.action_workoutFragment_to_workout_summary_fragment);
    }

    private final ActivityResultLauncher<Intent> chooseExerciseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Exercise exercise = result.getData().getParcelableExtra("exercise", Exercise.class);
                    workoutViewModel.addExercise(exercise);
                }
            });

    @Override
    public void onSetRemoved(ExerciseSet set, int adapterPosition, SetAdapter adapter) {
        int setPosition = set.getSetNumber() - 1; // Subtract 1 because the set number is 1-indexed
        workoutViewModel.removeSet(adapterPosition, setPosition);

        // Notify the adapter that the set was removed and all sets after it need to be updated
        if (setPosition >= 0 && setPosition <= adapter.getItemCount()) {
            adapter.notifyItemRangeChanged(setPosition, adapter.getItemCount() - setPosition + 1); // + 1 to account for the set that was removed
        }
    }

    @Override
    public void onSetAdded(int adapterPosition, SetAdapter adapter) {
        workoutViewModel.addSet(adapterPosition);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }

    @Override
    public void onSetToggleCompletion(ExerciseSet set, int position, SetAdapter adapter) {
        workoutViewModel.toggleSetCompletion(set);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onExerciseRemoved(Exercise exercise, int position) {
        workoutViewModel.removeExercise(position);
    }

}