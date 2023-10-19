package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    TextView errorMessage; // Add this TextView
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
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        errorMessage = findViewById(R.id.errorMessage); // Initialize the errorMessage TextView

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonLogin.setOnClickListener(View -> {
            progressBar.setVisibility(android.view.View.VISIBLE);
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                // Display an error message in the errorMessage TextView
                errorMessage.setText("Please enter a email");
                errorMessage.setVisibility(android.view.View.VISIBLE);
                progressBar.setVisibility(android.view.View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                // Display an error message in the errorMessage TextView
                errorMessage.setText("Please enter a password");
                errorMessage.setVisibility(android.view.View.VISIBLE);
                progressBar.setVisibility(android.view.View.GONE);
                return;
            }

            userViewModel.signIn(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(android.view.View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Display an error message in the errorMessage TextView
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                errorMessage.setText("Invalid email address.");
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage.setText("Invalid password.");
                            } else {
                                errorMessage.setText("Login failed.");
                            }

                            errorMessage.setVisibility(android.view.View.VISIBLE);
                        }
                    });
        });
    }
}
