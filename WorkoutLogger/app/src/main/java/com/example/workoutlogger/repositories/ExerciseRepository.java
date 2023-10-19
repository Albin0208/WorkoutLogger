package com.example.workoutlogger.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.data.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<Exercise>> exerciseData = new MutableLiveData<>();

    /**
     * Gets all exercises from Firestore
     *
     * @return A LiveData object containing a list of exercises
     */
    public LiveData<List<Exercise>> getExercises() {
        // Initialize with an empty list
        exerciseData.setValue(new ArrayList<>());

        db.collection("exercises").addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                // Handle the error
                Log.e("ExerciseRepository", "Error getting exercises", error);
            } else if (querySnapshot != null) {
                List<Exercise> exercises = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    Exercise exercise = document.toObject(Exercise.class);
                    if (exercise != null) {
                        exercise.setId(document.getId());
                        exercises.add(exercise);
                    }
                }
                exerciseData.setValue(exercises);
            }
        });

        return exerciseData;
    }

    /**
     * Creates an exercise in Firestore
     *
     * @param exercise             The exercise to create
     * @param onCompleteListener   The listener to call when the exercise is created
     */
    public void createExercise(Exercise exercise, OnCompleteListener<DocumentReference> onCompleteListener) {
        Map<String, Object> exerciseMap = new HashMap<>();
        exerciseMap.put("name", exercise.getName());

        db.collection("exercises").add(exerciseMap)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(e -> {
                    // Handle Firestore exception
                    Log.e("ExerciseRepository", "Error creating exercise", e);
                });
    }
}
