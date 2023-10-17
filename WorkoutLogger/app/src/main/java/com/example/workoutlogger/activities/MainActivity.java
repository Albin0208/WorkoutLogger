package com.example.workoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.workoutlogger.ExerciseViewModel;
import com.example.workoutlogger.R;
import com.example.workoutlogger.data.Exercise;
import com.example.workoutlogger.fragments.ExerciseListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Check if logged in
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the user from the database
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("MainActivity", "onCreate: " + task.getResult().getData());
            } else {
                Log.d("MainActivity", "Error getting documents: ", task.getException());
            }
        });

        // Get the exercises from the database
        db.collection("exercises").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Exercise> exercises = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    exercises.add(new Exercise(document.getData().get("name").toString()));
                }

                ExerciseViewModel exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
                exerciseViewModel.setExercises(exercises);

            } else {
                Log.d("MainActivity", "Error getting documents: ", task.getException());
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}