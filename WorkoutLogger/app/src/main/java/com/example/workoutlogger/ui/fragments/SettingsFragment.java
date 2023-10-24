package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        btnLogout.setOnClickListener(v -> {
            authViewModel.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}