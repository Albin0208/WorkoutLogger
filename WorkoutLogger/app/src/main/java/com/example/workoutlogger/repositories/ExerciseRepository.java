package com.example.workoutlogger.repositories;

import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.Record;
import com.example.workoutlogger.data.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

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
    public Single<Result<Exercise>> createUserExercise(Exercise exercise) {
        return Single.create(emitter -> {
            // Get the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onSuccess(Result.error(R.string.user_not_logged_in));
            } else {
                Map<String, Object> exerciseMap = new HashMap<>();
                exerciseMap.put("name", exercise.getName());
                exerciseMap.put("userCreated", exercise.isUserCreated());

                db.collection("users")
                        .document(currentUser.getUid())
                        .collection("exercises")
                        .add(exerciseMap)
                        .addOnSuccessListener(ref -> {
                            exercise.setId(ref.getId());
                            emitter.onSuccess(Result.success(exercise));
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ExerciseRepository", "Error creating exercise", e);

                            emitter.onSuccess(Result.error(R.string.unexpected_error_message));
                        });
            }
        });
    }

    public LiveData<Result<List<Record>>> getRecords(Exercise exercise) {
        MutableLiveData<Result<List<Record>>> records = new MutableLiveData<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            records.setValue(Result.error(R.string.user_not_logged_in));
        } else {
            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("records")
                    .document(exercise.getId())
                    .collection("records")
                    .get()
                    .addOnSuccessListener(documentSnapshots -> {
                        List<Record> recordsList = new ArrayList<>();
                        for (var snap : documentSnapshots.getDocuments())
                        {
                            Record record = snap.toObject(Record.class);
                            if (record != null) {
                                recordsList.add(record);
                            }
                        }
                        records.setValue(Result.success(recordsList));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ExerciseRepository", "Error getting records", e);
                        records.setValue(Result.error(R.string.unexpected_error_message));
                    });
        }

        return records;
    }

    /**
     * Deletes a user specific exercise from Firestore
     *
     * @param exercise The exercise to delete
     */
    public void deleteUserExercise(Exercise exercise) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        db.collection("users")
                .document(currentUser.getUid())
                .collection("exercises")
                .document(exercise.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("ExerciseRepository", "Exercise deleted");
                })
                .addOnFailureListener(e -> {
                    Log.e("ExerciseRepository", "Error deleting exercise", e);
                });
    }
}
