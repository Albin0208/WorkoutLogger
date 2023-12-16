package com.example.workoutlogger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Workout;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;

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

    public LiveData<Timestamp> getWorkoutDate() {
        return Transformations.map(workout, Workout::getDate);
    }

    public LiveData<String> getWorkoutName() {
        return Transformations.map(workout, Workout::getName);
    }

    public LiveData<List<Exercise>> getExercises() {
        return Transformations.map(workout, Workout::getExercises);
    }

}
