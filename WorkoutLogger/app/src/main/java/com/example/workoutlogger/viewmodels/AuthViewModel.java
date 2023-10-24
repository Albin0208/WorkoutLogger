package com.example.workoutlogger.viewmodels;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutlogger.R;
import com.example.workoutlogger.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<List<Pair<String, String>>> registerError = new MutableLiveData<>(new ArrayList<>());

    public AuthViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public boolean isUserSignedIn() {
        return userRepository.isUserSignedIn();
    }

    public void signIn(String email, String password) {
        userRepository.signIn(email, password);
    }

    public void register(String email, String password, String username) {
        // Validate the input
        List<Pair<String, String>> errors = new ArrayList<>();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add(new Pair<>("email", getApplication().getString(R.string.email_required)));
        }

        if (password.length() < 6) {
            errors.add(new Pair<>("password", getApplication().getString(R.string.password_length)));
        }

        if (username.isEmpty()) {
            errors.add(new Pair<>("username", getApplication().getString(R.string.username_required)));
        }

        if (!errors.isEmpty()) {
            registerError.setValue(errors);
            return;
        }
        // If the input is valid, register the user
        userRepository.createUser(email, password, username)
                .addOnSuccessListener(authResult -> {
                    registerSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    registerSuccess.setValue(false);

                    // Grab the error and check if it should be placed on a field
                    String errorCode = e.getLocalizedMessage();
                    String errorMessage = e.getMessage();
                    Log.e("ERRORVIEW", "register: ", e);

                    if (e instanceof FirebaseAuthUserCollisionException)
                        errors.add(new Pair<>("email", getApplication().getString(R.string.email_taken)));
                    else
                        errors.add(new Pair<>("general", errorMessage));

                    registerError.setValue(errors);
                });

        // Catch any errors

    }

    public void signOut() {
        userRepository.signOut();
    }

    public LiveData<List<Pair<String, String>>> getRegisterError() {
        return registerError;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }
}
