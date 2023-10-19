package com.example.workoutlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.data.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Gets the current user from Firebase Authentication
     *
     * @return A LiveData object containing the current user
     */
    public LiveData<User> getUserData() {
        MutableLiveData<User> userData = new MutableLiveData<>();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
//                            User user = documentSnapshot.toObject(User.class);
//                            userData.setValue(user);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                    });
        }
        return userData;
    }


    /**
     * Signs the user in to Firebase Authentication
     *
     * @param email    The user's email
     * @param password The user's password
     * @return A Task object containing the result of the operation
     */
    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    /**
     * Signs the user out of Firebase Authentication
     */
    public void signOut() {
        auth.signOut();
    }

    /**
     * Creates a user in Firebase Authentication and Firestore
     *
     * @param email    The user's email
     * @param password The user's password
     * @param username The user's username
     * @return A Task object containing the result of the operation
     */
    public Task<AuthResult> createUser(String email, String password, String username) {
        return auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("name", username);
                            db.collection("users").document(uid).set(userMap);
                        }
                    }
                });
    }

    /**
     * Checks if the user is signed in
     *
     * @return True if the user is signed in, false otherwise
     */
    public boolean isUserSignedIn() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }
}
