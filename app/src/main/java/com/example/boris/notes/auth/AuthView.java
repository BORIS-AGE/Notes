package com.example.boris.notes.auth;

interface AuthView {
    void showError(String error);
    void saveIdToPreferences(String id);
}
