package com.example.workoutlogger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btn;
    TextView textView;
    FirebaseUser user;

//    private RecyclerView recyclerView;
//    private ExerciseAdapter exerciseAdapter;
        BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> exercises = new ArrayList<>(Arrays.asList("Knäböj", "Bänkpress", "Marklyft"));
        auth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        recyclerView = findViewById(R.id.recycler_view);
//        exerciseAdapter = new ExerciseAdapter(exercises);
//        recyclerView.setAdapter(exerciseAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        btn = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);

        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
//            textView.setText(user.getEmail());
        }

//        btn.setOnClickListener(View -> {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        });
    }
}