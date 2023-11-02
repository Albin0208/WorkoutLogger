package com.example.workoutlogger.viewmodels;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.repositories.WorkoutRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WorkoutViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Result<Workout>> workoutCreatedResult = new MutableLiveData<>();

    public WorkoutViewModel() {
        Log.d("ViewModelLifecycle", "ViewModel created");
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    public void setExercises(List<Exercise> exercises) {
        exercisesLiveData.setValue(exercises);
    }

    /**
     * Adds an exercise to the workout
     *
     * @param exercise The exercise to add
     */
    public void addExercise(Exercise exercise) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        currentExercises.add(new Exercise(exercise));
        exercisesLiveData.setValue(currentExercises);
    }

    /**
     * Adds a set to the exercise at the given position
     *
     * @param position The position of the exercise to add the set to
     */
    public void addSet(int position) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        currentExercises.get(position).addSet(new ExerciseSet(0, 0, currentExercises.get(position).getSets().size() + 1));
        exercisesLiveData.setValue(currentExercises);
    }

    /**
     * Removes the set at the given position from the exercise at the given position
     *
     * @param position The position of the exercise to remove the set from
     * @param setPosition The position of the set to remove
     */
    public void removeSet(int position, int setPosition) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        Exercise exercise = currentExercises.get(position);
        exercise.removeSet(setPosition);
        currentExercises.set(position, exercise);
        exercisesLiveData.setValue(currentExercises);
    }

    /**
     * Removes the exercise passed
     *
     * @param position The position of the exercise to remove
     */
    public void removeExercise(int position) {
        List<Exercise> currentExercises = exercisesLiveData.getValue();
        currentExercises.remove(position);
        exercisesLiveData.setValue(currentExercises);
    }

    /**
     * Toggles the completion of the set passed
     *
     * @param set The set to toggle the completion of
     */
    public void toggleSetCompletion(ExerciseSet set) {
        set.setCompleted(!set.isCompleted());
    }

    public Workout getWorkout() {
        Workout workout = new Workout();
        workout.setExercises(exercisesLiveData.getValue());

        return workout;
    }

    @SuppressLint("CheckResult")
    public void saveWorkout(Workout workout) {
        WorkoutRepository workoutRepository = new WorkoutRepository();
        // TODO Save the workout to the database
        // Notify the user that the workout was saved
        workoutRepository.createWorkout(workout)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutCreatedResult::setValue);

    }

    public LiveData<Result<Workout>> getWorkoutCreatedResult() {
        return workoutCreatedResult;
    }
}
