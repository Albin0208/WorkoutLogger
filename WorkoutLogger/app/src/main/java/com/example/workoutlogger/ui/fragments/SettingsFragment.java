package com.example.workoutlogger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.workoutlogger.R;
import com.example.workoutlogger.ui.activities.LoginActivity;
import com.example.workoutlogger.viewmodels.UserViewModel;


public class SettingsFragment extends Fragment {
    private UserViewModel userViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        userViewModel = new UserViewModel();

        Button btnLogout = view.findViewById(R.id.logout_button);

        btnLogout.setOnClickListener(v -> {
            userViewModel.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}