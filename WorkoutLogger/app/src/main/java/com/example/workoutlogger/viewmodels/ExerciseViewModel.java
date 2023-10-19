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

        mergedExercises.addSource(globalExercises, exercises -> {
            // Handle global exercises (if needed)
            List<Exercise> mergedList = new ArrayList<>();
            // Merge logic here
            if (userExercises.getValue() != null) {
                mergedList.addAll(userExercises.getValue());
            }
            if (exercises != null) {
                mergedList.addAll(exercises);
            }
            mergedExercises.setValue(mergedList);
        });

        mergedExercises.addSource(userExercises, exercises -> {
            // Handle user-specific exercises (if needed)
            List<Exercise> mergedList = new ArrayList<>();
            // Merge logic here
            if (globalExercises.getValue() != null) {
                mergedList.addAll(globalExercises.getValue());
            }
            if (exercises != null) {
                mergedList.addAll(exercises);
            }
            mergedExercises.setValue(mergedList);
        });

        return mergedExercises;
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
