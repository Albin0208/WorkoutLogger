package com.example.workoutlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

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
                            user.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build());
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

    public LiveData<String> getUserName() {
        MutableLiveData<String> userData = new MutableLiveData<>();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userData.setValue(currentUser.getDisplayName());
        }
        return userData;
    }
}
