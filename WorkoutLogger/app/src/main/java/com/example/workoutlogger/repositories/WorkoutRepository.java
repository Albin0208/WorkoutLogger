package com.example.workoutlogger.repositories;

import com.example.workoutlogger.data.Result;
import com.example.workoutlogger.data.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Observable;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public WorkoutRepository() {

    }

    public Observable<Result<Workout>> createWorkout(Workout workout) {
        return Observable.create(emitter -> {
            // Grab the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                emitter.onNext(new Result<>(new Exception("User is not logged in")));
                return;
            }

            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("workouts")
                    .add(workout)
                    .addOnSuccessListener(documentReference -> {
                        emitter.onNext(new Result<>(workout));
                    })
                    .addOnFailureListener(e -> {
                        emitter.onNext(new Result<>(e));
                    });
        });


    }
}
