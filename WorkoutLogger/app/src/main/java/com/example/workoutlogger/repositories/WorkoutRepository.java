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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
                            .subscribe(result -> {
                                Log.d("WorkoutRepository", "createWorkout: " + result.isSuccess());
                                emitter.onSuccess(result.isSuccess() ? Result.success(workout) : Result.error(result.getError(), result.getErrorMessageRes()));
                            }))
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

            // Filter out invalid sets and records beaten by a later set, then process the rest
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

            // In case there are no valid sets, emit the result
            emitter.onSuccess(Result.success(exercise));
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
                List<Record> recordsList = convertDocumentsToRecords(result.getDocuments());

                List<Completable> completables = new ArrayList<>();
                // Check if the list is empty or if the new record is a new record and update the database
                if (recordsList.isEmpty() || recordsList.stream().noneMatch(record ->
                        record.getSet().getWeight() == newRecord.getSet().getWeight() || record.getSet().getReps() == newRecord.getSet().getReps())) {
                    completables.add(updateRecord(exerciseRecordDocument.document(), newRecord));
                } else {
                    boolean newRecordAdded = false;

                    // Find the and update the first record that is beaten by the new record and delete the rest that would be beaten by the new record
                    for (var record : recordsList) {
                        if (newRecord.checkIfNewRecord(record)) {
                            if (newRecordAdded) {
                                completables.add(deleteRecord(exerciseRecordDocument.document(record.getDocumentID())));
                            } else {
                                newRecordAdded = true;
                                completables.add(updateRecord(exerciseRecordDocument.document(record.getDocumentID()), newRecord));
                            }
                        }
                    }
                }

                // Wait for all the records to be updated then emit the result
                Completable.merge(completables)
                        .subscribe(
                                () -> emitter.onSuccess(Result.success(exercise)),
                                error -> emitter.onSuccess(Result.error(new Exception(), R.string.unexpected_error_message))
                        );
                return;
            }
        }

        emitter.onSuccess(Result.error(task.getException(), R.string.unexpected_error_message));
    }

    /**
     * Converts a list of documents to a list of records
     *
     * @param documents The documents to convert
     * @return A list of records
     */
    private List<Record> convertDocumentsToRecords(List<DocumentSnapshot> documents) {
        return documents.stream()
                .map(document ->  {
                    Record rec = document.toObject(Record.class);
                    if (rec != null)
                        rec.setDocumentID(document.getId());
                    return rec;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
     * Deletes a record in Firestore
     *
     * @param docRef The document reference to delete
     * @return An Observable object containing the result of the operation
     */
    private Completable deleteRecord(DocumentReference docRef) {
        return Completable.fromAction(docRef::delete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getWorkouts(int limit) {
        return fetchWorkouts(limit, null, null);
    }

    /**
     * Gets all workouts for the current user
     *
     * @param fromDate The date to get workouts from
     * @param toDate  The date to get workouts to
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getAllWorkouts(Date fromDate, Date toDate) {
        return fetchWorkouts(0, fromDate, toDate);
    }

    /**
     * Fetches workouts from Firestore
     *
     * @param limit    The maximum number of workouts to get, or 0 for no limit
     * @param fromDate The date to get workouts from
     * @param toDate  The date to get workouts to
     * @return An Observable object containing the result of the operation
     */
    private Single<Result<List<Workout>>> fetchWorkouts(int limit, Date fromDate, Date toDate) {
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

            if (fromDate != null)
                query = query.whereGreaterThanOrEqualTo("date", fromDate);

            if (toDate != null)
                query = query.whereLessThanOrEqualTo("date", toDate);

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
