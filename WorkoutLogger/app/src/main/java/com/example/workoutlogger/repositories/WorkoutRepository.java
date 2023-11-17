package com.example.workoutlogger.repositories;

import android.util.Log;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public WorkoutRepository() {}

    /**
     * Creates a workout in Firestore
     *
     * @param workout The workout to create
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<Workout>> createWorkout(Workout workout) {
        return Single.create(emitter -> {
            // Grab the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onSuccess(Result.error(new Exception(), R.string.user_not_logged_in));
                return;
            }

            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("workouts")
                    .add(workout)
                    .addOnSuccessListener(documentReference -> emitter.onSuccess(Result.success(workout)))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error creating workout", e);

                        emitter.onSuccess(Result.error(e, R.string.unexpected_error_message));
                    });
        });
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getWorkouts(int limit) {
        return Single.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                emitter.onSuccess(Result.error(new Exception(), R.string.user_not_logged_in));
                return;
            }


            Query query = buildWorkoutsQuery(currentUser.getUid(), limit);

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> emitter.onSuccess(Result.success(queryDocumentSnapshots.toObjects(Workout.class))))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error getting workouts", e);

                        emitter.onSuccess(Result.error(e, R.string.unexpected_error_message));
                    });
        });
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Single<Result<List<Workout>>> getAllWorkouts() {
        return Single.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onSuccess(Result.error(new Exception(), R.string.user_not_logged_in));
                return;
            }

            Query query = buildWorkoutsQuery(currentUser.getUid(), 0);

            query.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> emitter.onSuccess(Result.success(queryDocumentSnapshots.toObjects(Workout.class))))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error getting workouts", e);

                        emitter.onSuccess(Result.error(e, R.string.unexpected_error_message));
                    });
        });
    }

    /**
     * Builds a query for getting workouts
     *
     * @param UserID The user ID to get workouts for
     * @param limit  The maximum number of workouts to get, or 0 for no limit
     * @return A Query object for getting workouts
     */
    private Query buildWorkoutsQuery(String UserID, int limit) {
        Query query = db.collection("users")
                .document(UserID)
                .collection("workouts")
                .orderBy("date", Query.Direction.DESCENDING);

        if (limit > 0) {
            query = query.limit(limit);
        }

        return query;
    }
}
