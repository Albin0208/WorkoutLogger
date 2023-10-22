package com.example.workoutlogger.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>(new ArrayList<>());

    public WorkoutViewModel() {
        Log.d("ViewModelLifecycle", "ViewModel created");
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    public void setExercises(List<Exercise> exercises) {
        exercisesLiveData.setValue(exercises);
    }

    public void addExercise(Exercise exercise) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        currentExercises.add(exercise);
        exercisesLiveData.setValue(currentExercises);
    }
}
