package com.example.workoutlogger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.repositories.ExerciseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class ExerciseViewModel extends ViewModel {
    private final ExerciseRepository exerciseRepository;

    public ExerciseViewModel() {
        exerciseRepository = new ExerciseRepository();
    }

    public LiveData<List<Exercise>> getExercises() {
        MediatorLiveData<List<Exercise>> mergedExercises = new MediatorLiveData<>();
        LiveData<List<Exercise>> globalExercises = exerciseRepository.getGlobalExercises();
        LiveData<List<Exercise>> userExercises = exerciseRepository.getUserExercises();

        mergedExercises.addSource(globalExercises, exercises -> updateMergedData(mergedExercises, globalExercises, userExercises));

        mergedExercises.addSource(userExercises, exercises -> updateMergedData(mergedExercises, globalExercises, userExercises));

        return mergedExercises;
    }

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

    public LiveData<List<Exercise>> getUserExercises() {
        return exerciseRepository.getUserExercises();
    }

    public void createExercise(String name, OnCompleteListener<DocumentReference> onCompleteListener) {
        Exercise exercise = new Exercise(name);
        exerciseRepository.createExercise(exercise, onCompleteListener);
    }

    public void createUserExercise(String name, OnCompleteListener<DocumentReference> onCompleteListener) {
        Exercise exercise = new Exercise(name);
        exerciseRepository.createUserExercise(exercise, onCompleteListener);
    }
}
