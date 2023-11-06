package com.example.workoutlogger.repositories;

import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class ExerciseRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Gets all global exercises from Firestore
     *
     * @return A LiveData object containing a list of global exercises
     */
    public LiveData<List<Exercise>> getGlobalExercises() {
        MutableLiveData<List<Exercise>> exerciseData = new MutableLiveData<>();

        // Get the global exercises
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

                Log.d("ExerciseRepository", "Exercises: " + exercises);
                exerciseData.setValue(exercises);
            }
        });

        return exerciseData;
    }

    /**
     * Gets all user specific exercises from Firestore
     *
     * @return A LiveData object containing a list of user specific exercises
     */
    public LiveData<List<Exercise>> getUserExercises() {
        MutableLiveData<List<Exercise>> userExercises = new MutableLiveData<>();
        // Get the user specific exercises
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
            return userExercises;

        db.collection("users").document(currentUser.getUid()).collection("exercises")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("ExerciseRepository", "Error getting user exercises ", error);
                    } else if (querySnapshot != null) {
                        List<Exercise> exercises = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Exercise exercise = document.toObject(Exercise.class);
                            if (exercise != null) {
                                exercise.setId(document.getId());
                                exercises.add(exercise);
                            }
                        }
                        userExercises.setValue(exercises);
                    }
                });

        return userExercises;
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

    /**
     * Creates a user specific exercise in Firestore
     *
     * @param exercise The exercise to create
     *
     * @return An Observable object containing the created exercise
     */
    public Observable<Result<Exercise>> createUserExercise(Exercise exercise) {
        return Observable.create(emitter -> {
            // Get the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.user_not_logged_in))));
                emitter.onComplete();
            } else {
                Map<String, Object> exerciseMap = new HashMap<>();
                exerciseMap.put("name", exercise.getName());

                db.collection("users")
                        .document(currentUser.getUid())
                        .collection("exercises")
                        .add(exerciseMap)
                        .addOnSuccessListener(ref -> {
                            exercise.setId(ref.getId());
                            emitter.onNext(new Result<>(exercise));
                            emitter.onComplete();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ExerciseRepository", "Error creating exercise", e);

                            emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.unexpected_error_message))));
                        });
            }
        });
    }
}
