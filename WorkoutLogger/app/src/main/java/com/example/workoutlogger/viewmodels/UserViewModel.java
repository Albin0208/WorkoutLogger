package com.example.workoutlogger.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.example.workoutlogger.data.User;
import com.example.workoutlogger.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class UserViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData;

    public UserViewModel() {
        userRepository = new UserRepository();
        userLiveData = (MutableLiveData<User>) userRepository.getUserData();
    }

    public Task<AuthResult> registerUser(String email, String password, String username) {
        return userRepository.createUser(email, password, username);
    }

    public Task<AuthResult> signIn(String email, String password) {
        return userRepository.signIn(email, password);
    }

    public boolean isUserSignedIn() {
        return userRepository.isUserSignedIn();
    }

    public void signOut() {
        userRepository.signOut();
    }
}
