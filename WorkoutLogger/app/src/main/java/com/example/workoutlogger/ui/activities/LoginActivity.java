package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.AuthViewModel;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private TextView textView;
    private SignInButton googleSignInButton;

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
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Remove the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        googleSignInButton = findViewById(R.id.btn_google_sign_in);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        authViewModel.getAuthSuccess().observe(this, success -> {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getAuthError().observe(this, error -> {
            setUiForLogin(false);

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
                        errorMessage.setText(pair.second);
                        errorMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonLogin.setOnClickListener(View -> {
            setUiForLogin(true);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            authViewModel.signIn(email, password);
        });

        googleSignInButton.setOnClickListener(v -> {
            setUiForLogin(true);

            CancellationSignal cancellationSignal = new CancellationSignal();
            authViewModel.signInWithGoogle(cancellationSignal, this);
        });
    }

    private void setUiForLogin(boolean isLogin) {
        progressBar.setVisibility(isLogin ? android.view.View.VISIBLE : android.view.View.GONE);
        buttonLogin.setEnabled(!isLogin);
        googleSignInButton.setEnabled(!isLogin);
    }
}
