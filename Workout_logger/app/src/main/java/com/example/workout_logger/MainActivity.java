package com.example.workout_logger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button num_gen_button = findViewById(R.id.generator_button);
        TextView generated_num_text = findViewById(R.id.generated_number);
        EditText limit = findViewById(R.id.limit);

        num_gen_button.setOnClickListener(view -> {
            int number = 100;
            try {
                number = Integer.parseInt(limit.getText().toString());
            } catch (NumberFormatException e) {
                // We get exception continue with default value
            }
            generated_num_text.setText(Integer.toString(generateNumber(number)));
        });

    }

    private int generateNumber(int limit) {
        Random rand = new Random();

        return rand.nextInt(limit);
    }
}