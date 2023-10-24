package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextUsername;
    private Button buttonReg;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    public void onStart() {
        super.onStart();
        if (authViewModel.isUserSignedIn()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Remove the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        editTextEmail = findViewById(R.id.email);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(view -> navigateToLoginActivity());

        authViewModel.getRegisterSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
                navigateToLoginActivity();
            }
        });

        authViewModel.getRegisterError().observe(this, error -> {
            progressBar.setVisibility(View.GONE);
            buttonReg.setEnabled(true);

            boolean hasFocused = false;
            // Go through the error list and set all fields that have errors and the first field should be focused
            for (Pair<String, String> pair : error) {
                switch (pair.first) {
                    case "email":
                        editTextEmail.setError(pair.second);
                        if (!hasFocused) {
                            editTextEmail.requestFocus();
                            hasFocused = true;
                        }
                        break;
                    case "username":
                        editTextUsername.setError(pair.second);
                        if (!hasFocused) {
                            editTextUsername.requestFocus();
                            hasFocused = true;
                        }
                        break;
                    case "password":
                        editTextPassword.setError(pair.second);
                        if (!hasFocused) {
                            editTextPassword.requestFocus();
                            hasFocused = true;
                        }
                        break;
                    default:
                        // Error is not connected to any field but display anyways
                        TextView errorMessage = findViewById(R.id.errorMessage);
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText(pair.second);
                }
            }
        });

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            buttonReg.setEnabled(false);
            String email = String.valueOf(editTextEmail.getText());
            String username = String.valueOf(editTextUsername.getText());
            String password = String.valueOf(editTextPassword.getText());

            authViewModel.register(email, password, username);
        });
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
