package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mJwt;
    private String mUsername;
    private ArrayList<Integer> chatIdList;

    private UserInfoViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
        mUsername = "test1";
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getmJwt() {
        return mJwt;
    }

    public ArrayList<Integer> getChatIdList() {
        return chatIdList;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;

        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }

}

