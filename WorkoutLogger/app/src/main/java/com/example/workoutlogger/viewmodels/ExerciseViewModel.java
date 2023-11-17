package com.example.workoutlogger.viewmodels;

import android.annotation.SuppressLint;
import android.view.animation.Transformation;

import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.repositories.ExerciseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExerciseViewModel extends ViewModel {
    private final ExerciseRepository exerciseRepository;
    private final MutableLiveData<Result<Exercise>> exerciseCreatedResult;

    public ExerciseViewModel() {
        exerciseRepository = new ExerciseRepository();
        exerciseCreatedResult = new MutableLiveData<>();
    }

    /**
     * Gets all exercises from Firestore
     *
     * @return A LiveData object containing a list of exercises
     */
    public LiveData<List<Exercise>> getExercises() {
        MediatorLiveData<List<Exercise>> mergedExercises = new MediatorLiveData<>();
        LiveData<List<Exercise>> globalExercises = exerciseRepository.getGlobalExercises();
        LiveData<List<Exercise>> userExercises = exerciseRepository.getUserExercises();

        mergedExercises.addSource(globalExercises, exercises -> updateMergedData(mergedExercises, globalExercises, userExercises));
        mergedExercises.addSource(userExercises, exercises -> updateMergedData(mergedExercises, globalExercises, userExercises));

        return mergedExercises;
    }

    /**
     * Updates the merged data
     *
     * @param mergedExercises The merged exercises
     * @param globalExercises The global exercises
     * @param userExercises   The user exercises
     */
    private void updateMergedData(MediatorLiveData<List<Exercise>> mergedExercises, LiveData<List<Exercise>> globalExercises, LiveData<List<Exercise>> userExercises) {
        List<Exercise> mergedList = new ArrayList<>();

        List<Exercise> globalList = globalExercises.getValue();
        List<Exercise> userList = userExercises.getValue();

        if (globalList != null) {
            mergedList.addAll(globalList);
        }
        if (userList != null) {
            mergedList.addAll(userList);
        }

        mergedExercises.setValue(mergedList);
    }

    /**
     * Gets all users exercises from Firestore
     *
     * @return A LiveData object containing a list of users exercises
     */
    public LiveData<List<Exercise>> getUserExercises() {
        return exerciseRepository.getUserExercises();
    }

    /**
     * Creates a global exercise
     *
     * @param name                The name of the exercise to create
     * @param onCompleteListener The listener to call when the exercise is created
     */
    public void createExercise(String name, OnCompleteListener<DocumentReference> onCompleteListener) {
        Exercise exercise = new Exercise(name);
        exerciseRepository.createExercise(exercise, onCompleteListener);
    }

    /**
     * Creates a user specific exercise
     *
     * @param name The name of the exercise to create
     */
    @SuppressLint("CheckResult")
    public void createUserExercise(String name) {
        Exercise exercise = new Exercise(name);
        exerciseRepository.createUserExercise(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exerciseCreatedResult::setValue);
    }

    /**
     * Gets the result of the exercise creation
     *
     * @return A LiveData object containing the result of the exercise creation
     */
    public LiveData<Result<Exercise>> getExerciseCreatedResult() {
        return exerciseCreatedResult;
    }

    /**
     * Checks if the name is valid
     *
     * @param name The name to check
     * @return True if the name is valid, false otherwise
     */
    public boolean isValidName(String name) {
        return !name.isEmpty() && name.trim().length() > 0;
    }
}
