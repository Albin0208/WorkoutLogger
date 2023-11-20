package com.example.workoutlogger.repositories;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Record;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public WorkoutRepository() {}

    /**
     * Creates a workout in Firestore
     *
     * @param workout The workout to create
     * @return An Observable object containing the result of the operation
     */
    @SuppressLint("CheckResult")
    public Single<Result<Workout>> createWorkout(Workout workout) {
        return Single.create(emitter -> {
            // Grab the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onSuccess(Result.error(new Exception(), R.string.user_not_logged_in));
                return;
            }

            DocumentReference userDocument = db.collection("users").document(currentUser.getUid());

            userDocument.collection("workouts")
                    .add(workout)
                    .addOnSuccessListener(documentReference -> {
                        updateRecords(userDocument, workout.getExercises())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {
                                    if (result.isSuccess()) {
                                        emitter.onSuccess(Result.success(workout));
                                    } else {
                                        emitter.onSuccess(Result.error(result.getError(), result.getErrorMessageRes()));
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error creating workout", e);

                        emitter.onSuccess(Result.error(e, R.string.unexpected_error_message));
                    });
        });
    }

    /**
     * Updates the records for a workout
     *
     * @param userDocument The document reference to the user
     * @param exercises The workout to with all exercises
     * @return An Observable object containing the result of the operation
     */
    private Single<Result<Exercise>> updateRecords(DocumentReference userDocument, List<Exercise> exercises) {
        return Single.create(emitter -> {
            // Go through all the exercises in the workout
            CollectionReference recordsCollection = userDocument.collection("records");
            for (Exercise exercise : exercises) {
                DocumentReference exerciseDocument = recordsCollection.document(exercise.getId());

                for (ExerciseSet set : exercise.getSets()) {
                    // Check if the set is completed
                    if (!set.isCompleted()) continue;

                    CollectionReference exerciseRecordDocument = exerciseDocument.collection("records");
                    Record newRecord = new Record(exercise.getId(), set);

                    // Check if the record exists
                    exerciseRecordDocument.get()
                            .addOnCompleteListener(task -> handleResult(emitter, task, exercise, newRecord, exerciseRecordDocument));
                }
            }
        });
    }

    /**
     * Handles the result of the Firestore query
     *
     * @param emitter The emitter to emit the result to
     * @param task The task to handle
     * @param exercise The exercise
     * @param newRecord The new record to add
     * @param exerciseRecordDocument The document reference to update
     */
    @SuppressLint("CheckResult")
    private void handleResult(SingleEmitter<Result<Exercise>> emitter, Task<QuerySnapshot> task, Exercise exercise, Record newRecord, CollectionReference exerciseRecordDocument) {
        if (task.isSuccessful()) {
            QuerySnapshot result =  task.getResult();
            if (result != null) {
                // Convert to list of records
                List<Record> recordsList = result.getDocuments().stream()
                        .map(document ->  {
                            Record rec = document.toObject(Record.class);
                            rec.setDocumentID(document.getId());
                            return rec;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                // Check if the list is empty or if the new record is a new record and update the database
                if (recordsList.isEmpty() || recordsList.stream().noneMatch(record -> record.getSet().getWeight() == newRecord.getSet().getWeight() || record.getSet().getReps() == newRecord.getSet().getReps())) {
                    updateRecord(exerciseRecordDocument.document(), newRecord)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(res -> emitter.onSuccess(res.isSuccess() ? Result.success(exercise) : Result.error(res.getError(), res.getErrorMessageRes())));
                    return; // No need to check if it beats any records since it's the first record or a new record
                }

                // Check if it beats any records
                List<Single<Result<Record>>> updateRecords =
                        recordsList.stream()
                        .filter(newRecord::checkIfNewRecord)
                        .map(record -> updateRecord(exerciseRecordDocument.document(record.getDocumentID()), newRecord)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                        .collect(Collectors.toList());

                if (updateRecords.isEmpty()) {
                    emitter.onSuccess(Result.success(exercise));
                    return;
                }

                Single.concat(updateRecords)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> emitter.onSuccess(res.isSuccess() ? Result.success(exercise) : Result.error(res.getError(), res.getErrorMessageRes())));
            } else {
                emitter.onSuccess(Result.error(new Exception(), R.string.unexpected_error_message));
            }
        } else {
            emitter.onSuccess(Result.error(task.getException(), R.string.unexpected_error_message));
        }
    }

    /**
     * Updates or adds a record in Firestore
     *
     * @param docRef The document reference to update
     * @param record The record to update or add
     */
    private Single<Result<Record>> updateRecord(DocumentReference docRef, Record record) {
        return Single.create(emitter -> docRef.set(record)
                .addOnSuccessListener(t -> emitter.onSuccess(Result.success(record)))
                .addOnFailureListener(t -> emitter.onSuccess(Result.error(t, R.string.unexpected_error_message))));
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getWorkouts(int limit) {
        return fetchWorkouts(limit);
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getAllWorkouts() {
        return fetchWorkouts(0);
    }

    /**
     * Fetches workouts from Firestore
     *
     * @param limit The maximum number of workouts to get, or 0 for no limit
     * @return An Observable object containing the result of the operation
     */
    private Single<Result<List<Workout>>> fetchWorkouts(int limit) {
        return Single.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onSuccess(Result.error(new Exception(), R.string.user_not_logged_in));
                return;
            }

            Query query = db.collection("users")
                    .document(currentUser.getUid())
                    .collection("workouts")
                    .orderBy("date", Query.Direction.DESCENDING);

            if (limit > 0) {
                query = query.limit(limit);
            }

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> emitter.onSuccess(Result.success(queryDocumentSnapshots.toObjects(Workout.class))))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error getting workouts", e);

                        emitter.onSuccess(Result.error(e, R.string.unexpected_error_message));
                    });
        });
    }
}
