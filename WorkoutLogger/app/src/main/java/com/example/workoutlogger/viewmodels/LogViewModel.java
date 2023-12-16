package com.example.workoutlogger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Workout;

public class LogViewModel extends ViewModel {
    private final MutableLiveData<Workout> workout;

    public LogViewModel() {
        this.workout = new MutableLiveData<>();
    }

    public void setWorkout(Workout workout) {
        this.workout.setValue(workout);
    }

    public LiveData<Workout> getWorkout() {
        return workout;
    }

}
