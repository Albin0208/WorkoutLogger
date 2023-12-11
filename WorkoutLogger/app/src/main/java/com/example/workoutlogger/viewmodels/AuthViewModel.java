package com.example.workoutlogger.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.R;
import com.example.workoutlogger.repositories.UserRepository;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> authSuccess;
    private final MutableLiveData<List<Pair<String, String>>> authError;
    private SignInClient oneTapClient;

    public AuthViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        authSuccess = new MutableLiveData<>(false);
        authError = new MutableLiveData<>(new ArrayList<>());
    }

    /**
     * Get if the current user is signed in
     *
     * @return A boolean containing if the user is signed in
     */
    public boolean isUserSignedIn() {
        return userRepository.isUserSignedIn();
    }

    /**
     * Signs the user in to Firebase Authentication
     *
     * @param email    The user's email
     * @param password The user's password
     */
    public void signIn(String email, String password) {
        // Check if the input is valid
        List<Pair<String, String>> errors = new ArrayList<>();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add(new Pair<>("email", getApplication().getString(R.string.email_required)));
        }

        if (password.isEmpty()) {
            errors.add(new Pair<>("password", getApplication().getString(R.string.password_required)));
        }

        if (!errors.isEmpty()) {
            authError.setValue(errors);
            return;
        }

        userRepository.signIn(email, password)
                .addOnSuccessListener(authResult -> authSuccess.setValue(true))
                .addOnFailureListener(e -> {
                    authSuccess.setValue(false);

                    if (e instanceof FirebaseTooManyRequestsException)
                        errors.add(new Pair<>("general", getApplication().getString(R.string.too_many_requests)));
                    else
                        errors.add(new Pair<>("general", getApplication().getString(R.string.incorrect_email_or_password)));

                    authError.setValue(errors);
                });
    }

    /**
     * Registers a user with Firebase Authentication
     *
     * @param email    The user's email
     * @param password The user's password
     */
    public void register(String email, String password, String username) {
        // Validate the input
        List<Pair<String, String>> errors = new ArrayList<>();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add(new Pair<>("email", getApplication().getString(R.string.email_required)));
        }

        if (password.length() < 8) {
            errors.add(new Pair<>("password", getApplication().getString(R.string.password_length)));
        }

        if (username.isEmpty()) {
            errors.add(new Pair<>("username", getApplication().getString(R.string.username_required)));
        }

        if (!errors.isEmpty()) {
            authError.setValue(errors);
            return;
        }
        // If the input is valid, register the user
        userRepository.createUser(email, password, username)
                .addOnSuccessListener(authResult -> authSuccess.setValue(true))
                .addOnFailureListener(e -> {
                    authSuccess.setValue(false);

                    String errorMessage = e.getMessage();

                    if (e instanceof FirebaseAuthUserCollisionException)
                        errors.add(new Pair<>("email", getApplication().getString(R.string.email_taken)));
                    else
                        errors.add(new Pair<>("general", errorMessage));

                    authError.setValue(errors);
                });
    }

    /**
     * Signs the user out of Firebase Authentication
     */
    public void signOut() {
        userRepository.signOut();
    }

    /**
     * Get a LiveData object representing authentication error messages.
     * This LiveData will provide a list of pairs, where each pair consists of a field name
     * and an associated error message (if applicable).
     *
     * @return A LiveData object containing authentication error information.
     */
    public LiveData<List<Pair<String, String>>> getAuthError() {
        return authError;
    }

    /**
     * Get a LiveData object representing the authentication success status.
     * This LiveData will indicate whether an authentication action (e.g., sign-in or register)
     * was successful or not.
     *
     * @return A LiveData object containing the authentication success status.
     */
    public LiveData<Boolean> getAuthSuccess() {
        return authSuccess;
    }

    /**
     * Get the username of the current user
     *
     * @return A LiveData object containing the username of the current user
     */
    public LiveData<String> getUsername() {
        return userRepository.getUserName();
    }

    public Task<BeginSignInResult> startSignIn() {
        oneTapClient = Identity.getSignInClient(getApplication());
        return oneTapClient.beginSignIn(buildSignInRequest())
                .addOnFailureListener(e -> {
                    List<Pair<String, String>> errors = new ArrayList<>();
                    errors.add(new Pair<>("general", getApplication().getString(R.string.unexpected_error_message)));

                    authError.setValue(errors);
                });
    }

    public void signInWithIntent(Intent data) {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
            userRepository.signInWithGoogle(authCredential)
                    .addOnSuccessListener(authResult -> {
                        authSuccess.setValue(true);
                        if (authResult.getAdditionalUserInfo().isNewUser()) // If new user update username
                            authResult.getUser().updateProfile(
                                    new UserProfileChangeRequest.Builder()
                                        .setDisplayName(credential.getGivenName())
                                    .build());

                        // TODO: Link accounts of the same email

                    })
                    .addOnFailureListener(e -> {
                        authSuccess.setValue(false);

                        List<Pair<String, String>> errors = new ArrayList<>();
                        errors.add(new Pair<>("general", getApplication().getString(R.string.unexpected_error_message)));

                        authError.setValue(errors);
                    });
        } catch (ApiException e) {
            // Display error to user
            authSuccess.setValue(false);

            List<Pair<String, String>> errors = new ArrayList<>();
            Log.e("AuthViewModel", e.getMessage());
            errors.add(new Pair<>("general", getApplication().getString(R.string.unexpected_error_message)));

            authError.setValue(errors);

            throw new RuntimeException(e);
        }
    }

    private BeginSignInRequest buildSignInRequest() {
        return BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(getApplication().getString(R.string.default_web_client_id))
                                .build()
                )
                .setAutoSelectEnabled(true)
                .build();
    }
}
