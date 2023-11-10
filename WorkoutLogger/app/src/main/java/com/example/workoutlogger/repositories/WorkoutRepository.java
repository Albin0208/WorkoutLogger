package com.example.workoutlogger.repositories;

import android.content.res.Resources;
import android.util.Log;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public WorkoutRepository() {}

    /**
     * Creates a workout in Firestore
     *
     * @param workout The workout to create
     * @return An Observable object containing the result of the operation
     */
    public Observable<Result<Workout>> createWorkout(Workout workout) {
        return Observable.create(emitter -> {
            // Grab the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.user_not_logged_in))));
                return;
            }

            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("workouts")
                    .add(workout)
                    .addOnSuccessListener(documentReference -> emitter.onNext(new Result<>(workout)))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error creating workout", e);

                        emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.unexpected_error_message))));
                    });
        });


    }

    public Observable<Result<List<Workout>>> getWorkouts() {
        return getWorkouts(0);
    }

    /**
     * Gets all workouts for the current user
     *
     * @return An Observable object containing the result of the operation
     */
    public Observable<Result<List<Workout>>> getWorkouts(int limit) {
        return Observable.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.user_not_logged_in))));
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
                    .addOnSuccessListener(queryDocumentSnapshots -> emitter.onNext(new Result<>(queryDocumentSnapshots.toObjects(Workout.class))))
                    .addOnFailureListener(e -> {
                        Log.e("WorkoutRepository", "Error getting workouts", e);

                        emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.unexpected_error_message))));
                    });
//
//            db.collection("users")
//                    .document(currentUser.getUid())
//                    .collection("workouts")
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> emitter.onNext(new Result<>(queryDocumentSnapshots.toObjects(Workout.class))))
//                    .addOnFailureListener(e -> {
//                        Log.e("WorkoutRepository", "Error getting workouts", e);
//
//                        emitter.onNext(new Result<>(new Exception(Resources.getSystem().getString(R.string.unexpected_error_message))));
//                    });
        });

    }
}
