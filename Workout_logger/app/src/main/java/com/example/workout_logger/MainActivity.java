package com.example.workout_logger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button numGenButton = findViewById(R.id.generator_button);
        final Button swapActivity = findViewById(R.id.swap_activity);
        TextView generated_num_text = findViewById(R.id.generated_number);
        EditText limit = findViewById(R.id.limit);

        numGenButton.setOnClickListener(view -> {
            int number = 100;
            try {
                number = Integer.parseInt(limit.getText().toString());
            } catch (NumberFormatException e) {
                // We get exception continue with default value
            }
            generated_num_text.setText(Integer.toString(generateNumber(number)));
        });

        swapActivity.setOnClickListener( view -> {
            // Create a new intent to the second activity
            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

            // Pass the generated number
            intent.putExtra("rand_num", generated_num_text.getText().toString());

            // Start the other activity
            startActivity(intent);
        });
    }

    private int generateNumber(int limit) {
        Random rand = new Random();

        return rand.nextInt(limit);
    }
}