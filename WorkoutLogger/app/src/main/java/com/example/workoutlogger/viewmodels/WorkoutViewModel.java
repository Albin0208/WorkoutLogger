package com.example.workoutlogger.viewmodels;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.example.workoutlogger.repositories.WorkoutRepository;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WorkoutViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Result<Workout>> workoutCreatedResult = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Workout>>> workoutsLiveData = new MutableLiveData<>(Result.success(new ArrayList<>()));
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(true);
    private final MutableLiveData<String> workoutName = new MutableLiveData<>();
    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    private Workout workout;

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
        Exercise e = new Exercise(exercise);
        e.addSet(new ExerciseSet(0, 0, 1));
        currentExercises.add(e);
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
        if (exercise.getSets().size() == 1) {
            currentExercises.remove(position);
        } else
            exercise.removeSet(setPosition);
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


    /**
     * Gets the workout that was created
     *
     * @return The workout that was created
     */
    public Workout getWorkout() {
        Workout workout = new Workout();
        workout.setExercises(exercisesLiveData.getValue());
        workout.setName(workoutName.getValue());

        return workout;
    }

    /**
     * Gets all workouts for the current user
     */
    @SuppressLint("CheckResult")
    public void getWorkouts(int numWorkouts) {
        isLoading.postValue(true);

        workoutRepository.getWorkouts(numWorkouts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    isLoading.postValue(false);
                    if (response.isSuccess()) {
                        workoutsLiveData.postValue(Result.success(response.getData()));
                        isEmpty.postValue(response.getData().isEmpty());
                        return;
                    }
                    isEmpty.postValue(true);
                    workoutsLiveData.postValue(response);
                });
    }

    /**
     * Gets all workouts for the current user
     */
    @SuppressLint("CheckResult")
    public void getAllWorkouts() {
        isLoading.postValue(true);

        workoutRepository.getAllWorkouts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            isLoading.postValue(false);
                            if (response.isSuccess()) {
                                workoutsLiveData.postValue(Result.success(response.getData()));
                                isEmpty.postValue(response.getData().isEmpty());
                                return;
                            }

                            workoutsLiveData.postValue(response);
                        });
    }

    @SuppressLint("CheckResult")
    public void saveWorkout() {
        Workout workout = getWorkout();
        workout.setDate(Timestamp.now());

        // Notify the user that the workout was saved
        workoutRepository.createWorkout(workout)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutCreatedResult::postValue);
    }

    /**
     * Gets the result of the workout that was created
     *
     * @return The result of the workout that was created
     */
    public LiveData<Result<Workout>> getWorkoutCreatedResult() {
        return workoutCreatedResult;
    }

    /**
     * Gets the result of the workouts
     *
     * @return The result of the workouts
     */
    public LiveData<Result<List<Workout>>> getWorkoutsLiveData() {
        return workoutsLiveData;
    }

    /**
     * Gets whether the workouts are loading or not
     *
     * @return Whether the workouts are loading or not
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Gets whether the workouts are empty or not
     *
     * @return Whether the workouts are empty or not
     */
    public LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    public void setWorkoutName(String string) {
        workoutName.setValue(string);
    }

    public LiveData<String> getWorkoutName() {
        return workoutName;
    }
}
