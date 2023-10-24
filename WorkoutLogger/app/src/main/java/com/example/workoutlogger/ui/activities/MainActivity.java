package com.example.workoutlogger.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.workoutlogger.R;
import com.example.workoutlogger.viewmodels.AuthViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Check if logged in
        if (!authViewModel.isUserSignedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}