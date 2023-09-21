package com.example.workout_logger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        final Button btnAddName = findViewById(R.id.btn_add_name);
        final Button swapActivity = findViewById(R.id.swap_activity);
        EditText nameToAdd = findViewById(R.id.et_name);

        btnAddName.setOnClickListener(view -> {
            String nameValue = nameToAdd.getText().toString().trim();

            if (!TextUtils.isEmpty(nameValue)) {
                Map<String, Object> name = new HashMap<>();
                name.put("name", nameValue);

                addToDB(name);
            } else {
                // Handle the case where the input is empty or contains only whitespace
                Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            }
        });

        swapActivity.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

            startActivity(intent);
        });
    }

    private void addToDB(Map<String, Object> name) {
        db.collection("names")
                .add(name)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Name added", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to add name", Toast.LENGTH_LONG).show();
                });
    }
}