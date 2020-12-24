package com.example.boris.notes.auth;

import androidx.annotation.StringRes;
import io.reactivex.functions.Action;

interface AuthView {
    void showError(String error);
    void saveIdToPreferences(String id);

    void openMainActivity();

    void showError(@StringRes int resId);

}
