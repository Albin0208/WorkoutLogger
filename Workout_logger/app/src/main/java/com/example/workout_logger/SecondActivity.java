package com.example.workout_logger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final Button swap_button = findViewById(R.id.swap_activity);

        // Fill the text with the generated number if exist, else display no number generated
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("rand_num");

            if (!Objects.equals(value, "Number")) {
                TextView passedExtra = findViewById(R.id.gened_number);
                passedExtra.setText(value);
            }
        }

        swap_button.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
        });
    }
}