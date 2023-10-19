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
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToLoginActivity();
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
        createUser(email, password, username);
    }

    private void createUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    buttonReg.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
                        saveUserToDatabase(username);
                    } else {
                        Log.e("Creating user", "Registration failed: " + task.getException());
                        Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String username) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", username);

            db.collection("users").document(user.getUid()).set(userMap)
                    .addOnSuccessListener(documentReference -> {
                        navigateToLoginActivity();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Creating user", "Error saving user data: " + e);
                        Toast.makeText(this, "Error saving user data. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
