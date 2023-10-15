package com.example.workoutlogger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;

import java.util.List;

public class ExerciseViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    public void setExercises(List<Exercise> exercises) {
        exercisesLiveData.setValue(exercises);
    }
}
