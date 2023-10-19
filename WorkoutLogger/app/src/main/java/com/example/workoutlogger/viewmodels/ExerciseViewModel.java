package com.example.workoutlogger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.repositories.ExerciseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ExerciseViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> exercisesLiveData;
    private ExerciseRepository exerciseRepository;

    public ExerciseViewModel() {
        exerciseRepository = new ExerciseRepository();
        exercisesLiveData = (MutableLiveData<List<Exercise>>) exerciseRepository.getExercises();
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    public void setExercises(List<Exercise> exercises) {
        exercisesLiveData.setValue(exercises);
    }

    public void createExercise(String name, OnCompleteListener<DocumentReference> onCompleteListener) {
        Exercise exercise = new Exercise(name);
        exerciseRepository.createExercise(exercise, onCompleteListener);
    }
}
