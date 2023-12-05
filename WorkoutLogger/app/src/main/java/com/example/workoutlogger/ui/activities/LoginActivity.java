package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.AuthViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
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

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = accountTask.getResult(ApiException.class);

                    if (account != null) {
                        authViewModel.signInWithGoogle(account);
                    }
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Remove the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextInputEditText editTextEmail = findViewById(R.id.email);
        TextInputEditText editTextPassword = findViewById(R.id.password);
        Button buttonLogin = findViewById(R.id.btn_login);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.registerNow);
        SignInButton googleSignInButton = findViewById(R.id.btn_google_sign_in);

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
            progressBar.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);

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
            progressBar.setVisibility(android.view.View.VISIBLE);
            buttonLogin.setEnabled(false);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            authViewModel.signIn(email, password);
        });

        googleSignInButton.setOnClickListener(v -> {
            progressBar.setVisibility(android.view.View.VISIBLE);
            buttonLogin.setEnabled(false);
            googleSignInButton.setEnabled(false);

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, authViewModel.getGoogleSignInOptions());

            signInLauncher.launch(googleSignInClient.getSignInIntent());
        });
    }
}
