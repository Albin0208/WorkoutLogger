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
import com.google.firebase.FirebaseTooManyRequestsException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
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

        // Remove the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextInputEditText editTextEmail = findViewById(R.id.email);
        TextInputEditText editTextPassword = findViewById(R.id.password);
        Button buttonLogin = findViewById(R.id.btn_login);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.registerNow);
        TextView errorMessage = findViewById(R.id.errorMessage);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonLogin.setOnClickListener(View -> {
            progressBar.setVisibility(android.view.View.VISIBLE);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

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
                            Exception e = task.getException();
                            Log.e("LoginActivity", "Login failed", e);

                            if (e instanceof FirebaseTooManyRequestsException)
                                errorMessage.setText(R.string.too_many_requests);
                            else
                                errorMessage.setText(R.string.incorrect_email_or_password);



                            errorMessage.setVisibility(android.view.View.VISIBLE);
                        }
                    });
        });
    }
}
