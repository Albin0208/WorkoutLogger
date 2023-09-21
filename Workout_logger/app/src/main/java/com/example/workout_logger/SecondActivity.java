package com.example.workout_logger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ListAdapter listAdapter; // Declare the adapter here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        db = FirebaseFirestore.getInstance();
        List<String> names = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        listAdapter = new ListAdapter(names); // Initialize the adapter here
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db.collection("names")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            if (name != null)
                                names.add(name);
                        }
                        Collections.sort(names); // Sort the names in alphabetical order
                        listAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });

        final Button swap_button = findViewById(R.id.swap_activity);

        swap_button.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}
