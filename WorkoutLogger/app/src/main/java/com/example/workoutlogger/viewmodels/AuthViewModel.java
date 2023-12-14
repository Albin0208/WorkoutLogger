package com.example.workoutlogger.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.R;
import com.example.workoutlogger.repositories.UserRepository;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> authSuccess;
    private final MutableLiveData<List<Pair<String, String>>> authError;

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

    /**
     * Signs the user in with Google Sign In
     *
     * @param cancellationSignal The cancellation signal
     * @param activity          The activity, used for the credential manager
     */
    public void signInWithGoogle(CancellationSignal cancellationSignal, Activity activity) {
        CredentialManager credentialManager = CredentialManager.create(activity);

        credentialManager.getCredentialAsync(
                activity,
                getSignInRequest(),
                cancellationSignal,
                Runnable::run,
                getCredentialManagerCallback()
        );
    }

    /**
     * Get the sign in request for Google Sign In
     *
     * @return A GetCredentialRequest object containing the sign in request
     */
    private GetCredentialRequest getSignInRequest() {
        var googleIdOption = new GetSignInWithGoogleOption.Builder(getApplication().getString(R.string.default_web_client_id))
                .build();

        return new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
    }

    /**
     * Get the callback for the credential manager
     *
     * @return A CredentialManagerCallback object containing the callback
     */
    private CredentialManagerCallback<GetCredentialResponse, GetCredentialException> getCredentialManagerCallback() {
        return new CredentialManagerCallback<>() {
            @Override
            public void onResult(GetCredentialResponse getCredentialResponse) {
                Credential credential = getCredentialResponse.getCredential();
                if (credential instanceof CustomCredential) {
                    if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.getData());
                        userRepository.signInWithGoogle(googleIdTokenCredential)
                                .addOnSuccessListener(authResult -> authSuccess.setValue(true))
                                .addOnFailureListener(e -> {
                                    authSuccess.setValue(false);

                                    List<Pair<String, String>> errors = new ArrayList<>();
                                    errors.add(new Pair<>("general", e.getMessage()));

                                    authError.postValue(errors);
                                });
                    }
                }
            }

            @Override
            public void onError(@NonNull GetCredentialException e) {
                List<Pair<String, String>> errors = new ArrayList<>();

                Log.e("AuthViewModel", "OnError: ", e);

                // If this is a cancellation exception, do nothing
                if (!(e instanceof GetCredentialCancellationException))
                    errors.add(new Pair<>("general", getApplication().getString(R.string.unexpected_error_message)));

                authError.postValue(errors);
            }
        };
    }
}
