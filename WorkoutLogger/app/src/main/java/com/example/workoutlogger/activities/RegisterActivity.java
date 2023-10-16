package com.example.workoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
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

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            startActivity(intent);
            finish();
        });

        buttonReg.setOnClickListener(view -> {
            String username = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(username)) {
                editTextEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required");
                return;
            }

            editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editTextEmail.setError(null); // Clear the error when the field is focused.
                }
            });

            editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    editTextPassword.setError(null); // Clear the error when the field is focused.
                }
            });
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Account created.",
                                Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }
}