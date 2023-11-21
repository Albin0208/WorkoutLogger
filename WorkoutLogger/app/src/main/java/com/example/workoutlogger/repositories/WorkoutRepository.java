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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
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
                    .addOnSuccessListener(documentReference -> updateRecords(userDocument, workout.getExercises())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> emitter.onSuccess(result.isSuccess() ? Result.success(workout) : Result.error(result.getError(), result.getErrorMessageRes()))))
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
     * @param exercises    The workout to with all exercises
     * @return An Observable object containing the result of the operation
     */
    private @NonNull Single<Result<?>> updateRecords(DocumentReference userDocument, List<Exercise> exercises) {
        return Observable.fromIterable(exercises)
                .flatMap(exercise -> updateExercise(userDocument, exercise).toObservable())
                .toList()
                .map(results -> {
                    // Check for errors
                    for (Result<Exercise> result : results) {
                        if (!result.isSuccess()) {
                            return Result.error(result.getError(), result.getErrorMessageRes());
                        }
                    }
                    // No error found
                    return Result.success(results);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Updates the records for an exercise
     *
     * @param userDocument The document reference to the user
     * @param exercise     The exercise to update
     * @return An Observable object containing the result of the operation
     */
    private Single<Result<Exercise>> updateExercise(DocumentReference userDocument, Exercise exercise) {
        return Single.create(emitter -> {
            CollectionReference recordsCollection = userDocument.collection("records");
            DocumentReference exerciseDocument = recordsCollection.document(exercise.getId());
            CollectionReference exerciseRecordDocument = exerciseDocument.collection("records");

            // Filter and process completed sets with positive weights and reps
            exercise.getSets()
                    .stream()
                    .filter(this::isValidSet)
                    .collect(Collectors.toMap(ExerciseSet::getWeight, Function.identity(), BinaryOperator.maxBy(Comparator.comparingDouble(ExerciseSet::getReps)))) // Filter out the sets with the same weight and keep the one with the highest reps
                    .values()
                    .stream()
                    .collect(Collectors.toMap(ExerciseSet::getReps, Function.identity(), BinaryOperator.maxBy(Comparator.comparingDouble(ExerciseSet::getWeight)))) // Filter out the sets with the same reps and keep the one with the highest weight
                    .values()
                    .forEach(set -> {
                        Record newRecord = new Record(exercise.getId(), set);

                        exerciseRecordDocument.get()
                                .addOnCompleteListener(task -> handleResult(emitter, task, exercise, newRecord, exerciseRecordDocument))
                                .addOnFailureListener(e -> emitter.onSuccess(Result.error(e, R.string.unexpected_error_message)));
                    });
        });
    }

    /**
     * Checks if a set is valid
     *
     * @param set The set to check
     * @return True if the set is valid, false otherwise
     */
    private boolean isValidSet(ExerciseSet set) {
        return set.isCompleted() && set.getWeight() > 0 && set.getReps() > 0;
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
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> emitter.onSuccess(Result.success(exercise)),
                                    error -> emitter.onSuccess(Result.error(new Exception(), R.string.unexpected_error_message))
                            );
                    return; // No need to check if it beats any records since it's the first record or a new record
                }

                // TODO If two records have the same weight and reps, only keep the one with the latest timestamp

                /*
                 * Check if the new record beats any records
                 * If it does, update the records
                 * Subscribe to the updating of the records to be able to wait for all the records to be updated
                */
                List<Completable> updateRecords = recordsList.stream()
                        .filter(newRecord::checkIfNewRecord)
                        .map(record -> updateRecord(exerciseRecordDocument.document(record.getDocumentID()), newRecord)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                        .collect(Collectors.toList());

                if (updateRecords.isEmpty()) {
                    emitter.onSuccess(Result.success(exercise));
                    return;
                }

                // Wait for all the records to be updated then emit the result
                Completable.concat(updateRecords)
                        .subscribe(
                                () -> emitter.onSuccess(Result.success(exercise)),
                                error -> emitter.onSuccess(Result.error(new Exception(), R.string.unexpected_error_message))
                        );
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
     * @return An Observable object containing the result of the operation
     */
    private Completable updateRecord(DocumentReference docRef, Record record) {
        return Completable.fromAction(() -> docRef.set(record))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
