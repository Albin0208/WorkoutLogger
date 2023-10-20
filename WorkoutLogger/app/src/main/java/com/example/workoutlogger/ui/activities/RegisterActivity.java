package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    private Button buttonReg;
    private ProgressBar progressBar;
    private final UserViewModel userViewModel = new UserViewModel();

    @Override
    public void onStart() {
        super.onStart();
        if (userViewModel.isUserSignedIn()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Remove the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        editTextEmail = findViewById(R.id.email);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(view -> navigateToLoginActivity());

        buttonReg.setOnClickListener(view -> registerUser());
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) || password.length() >= 8;
    }

    private void registerUser() {
        String email = String.valueOf(editTextEmail.getText());
        String username = String.valueOf(editTextUsername.getText());
        String password = String.valueOf(editTextPassword.getText());

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.invalid_email));
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.username_required));
            editTextUsername.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.password_length));
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonReg.setEnabled(false);

        userViewModel.registerUser(email, password, username)
                .addOnCompleteListener(task -> {
                    // Redirect to login activity
                    progressBar.setVisibility(View.GONE);
                    buttonReg.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
                        navigateToLoginActivity();
                    } else {
                        Exception exception = task.getException();
                        Log.e("Creating user", "Registration failed: " + exception);
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            editTextEmail.setError("Invalid email address.");
                            editTextEmail.requestFocus();
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            editTextPassword.setError("Invalid password.");
                            editTextPassword.requestFocus();
                        } else if (exception instanceof FirebaseAuthUserCollisionException) {
                            editTextEmail.setError(exception.getMessage());
                            editTextEmail.requestFocus();
                        } else {
                            TextView errorMessage = findViewById(R.id.errorMessage);
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("Registration failed. Please try again.");
                        }
                    }
                });
    }
}
