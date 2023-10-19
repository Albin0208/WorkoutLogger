package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    TextView errorMessage;
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
        errorMessage = findViewById(R.id.errorMessage);

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

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError(getString(R.string.email_required));
                editTextEmail.requestFocus();

                progressBar.setVisibility(android.view.View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError(getString(R.string.password_required));
                editTextPassword.requestFocus();

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
                            Log.e("LoginActivity", "Login failed", task.getException());

                            errorMessage.setText(R.string.incorrect_email_or_password);
                            errorMessage.setVisibility(android.view.View.VISIBLE);
                        }
                    });
        });
    }
}
