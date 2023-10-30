package com.example.workoutlogger.viewmodels;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutViewModel extends ViewModel {
    private View.OnClickListener listener;

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

    public void updateExerciseSets(List<Exercise> currentList, int position) {

    }

    public void addSet(int position) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        Exercise exercise = currentExercises.get(position);
        exercise.addSet();
        currentExercises.set(position, exercise);
        exercisesLiveData.setValue(currentExercises);
    }

    public void removeSet(int position, int setNumber) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        Exercise exercise = currentExercises.get(position);
        exercise.removeSet(setNumber);
        currentExercises.set(position, exercise);
        exercisesLiveData.setValue(currentExercises);
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public LiveData<List<ExerciseSet>> getExerciseSets(Exercise exercise) {
        return (LiveData<List<ExerciseSet>>) exercise.getSets();
    }
}
