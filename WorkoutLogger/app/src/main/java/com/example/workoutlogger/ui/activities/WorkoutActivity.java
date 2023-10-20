package com.example.workoutlogger.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutlogger.R;

import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        View view = getSupportActionBar().getCustomView();


        ImageButton abortWorkout = view.findViewById(R.id.abort_workout);
        ImageButton finishWorkout = view.findViewById(R.id.finish_workout);

        abortWorkout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_abort_workout, null);

            Button yesButton = customLayout.findViewById(R.id.confirm_button);
            Button noButton = customLayout.findViewById(R.id.cancel_button);

            builder.setView(customLayout);
            AlertDialog dialog = builder.create();

            // Apply the custom background to the dialog's window
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.rounded_dialog_bg); // Reference to your custom background drawable
            }

            yesButton.setOnClickListener(v1 -> finish());

            noButton.setOnClickListener(v1 -> dialog.dismiss());


            dialog.show();
        });


        finishWorkout.setOnClickListener(v -> {
            // TODO Implement finishing the workout
        });
    }
}