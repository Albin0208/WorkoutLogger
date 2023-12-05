package com.example.workoutlogger.ui.fragments;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutlogger.R;
import com.example.workoutlogger.ui.activities.LoginActivity;
import com.example.workoutlogger.viewmodels.AuthViewModel;


public class SettingsFragment extends Fragment {
    private AuthViewModel authViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Button btnLogout = view.findViewById(R.id.logout_button);
        SwitchCompat switchCompat = view.findViewById(R.id.dark_mode_toggle);
        TextView username = view.findViewById(R.id.username_text_view);

        authViewModel.getUsername().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                username.setText(user);
            }
        });

        // Get the current theme of the app
        int currentTheme = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        // Set the switch to the current theme
        switchCompat.setChecked(currentTheme == android.content.res.Configuration.UI_MODE_NIGHT_YES);

        // Set the switch to change the theme
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UiModeManager uiModeManager = requireContext().getSystemService(UiModeManager.class);
            uiModeManager.setApplicationNightMode(isChecked ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO);
        });

        btnLogout.setOnClickListener(v -> {
            authViewModel.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}