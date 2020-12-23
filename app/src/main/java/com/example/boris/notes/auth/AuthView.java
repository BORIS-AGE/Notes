package com.example.boris.notes.auth;

import io.reactivex.functions.Action;

interface AuthView {
    void showError(String error);
    void saveIdToPreferences(String id);

    void openMainActivity();

}
