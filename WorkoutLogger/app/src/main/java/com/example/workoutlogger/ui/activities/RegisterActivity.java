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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    private Button buttonReg;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private void registerUser() {
        String email = String.valueOf(editTextEmail.getText());
        String username = String.valueOf(editTextUsername.getText());
        String password = String.valueOf(editTextPassword.getText());

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            editTextEmail.setError("Valid email is required");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            editTextPassword.setError("Password is required and must be at least 8 characters long");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonReg.setEnabled(false);

        // TODO Add a check if username is already taken
        userViewModel.registerUser(email, password, username)
                .addOnCompleteListener(task -> {
                    // Redirect to login activity
                    progressBar.setVisibility(View.GONE);
                    buttonReg.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
                        navigateToLoginActivity();
                    } else {
                        Log.e("Creating user", "Registration failed: " + task.getException());
                        Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
