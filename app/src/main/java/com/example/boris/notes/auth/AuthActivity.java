package com.example.boris.notes.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import com.example.boris.notes.Constants;
import com.example.boris.notes.MainActivity;
import com.example.boris.notes.R;
import com.google.android.material.snackbar.Snackbar;

public class AuthActivity extends AppCompatActivity implements AuthView {

    private AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme_ActionBarStyle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        presenter = new AuthPresenter(this, getApplicationContext());
        if (!getSharedPreferences(Constants.PREFERENCES_KEY, MODE_PRIVATE).getString(Constants.USER_ID, "").equals("")){
            openMainActivity();
        }

        findViewById(R.id.submitButton).setOnClickListener((view) -> {
            presenter.saveValue();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatEditText emailET = findViewById(R.id.emailEditText);
        AppCompatEditText passwordET = findViewById(R.id.passwordEditText);
        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                presenter.fieldEntered(charSequence.toString(), AuthPresenter.FieldType.EMAIL);
            }

            @Override
            public void afterTextChanged(final Editable editable) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                presenter.fieldEntered(charSequence.toString(), AuthPresenter.FieldType.PASSWORD);
            }

            @Override
            public void afterTextChanged(final Editable editable) {

            }
        });
    }

    @Override
    public void showError(final String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveIdToPreferences(final String id) {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFERENCES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.USER_ID, id);
        ed.commit();
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showError(final int resId) {
        Snackbar.make(findViewById(R.id.rootView), getString(resId), Snackbar.LENGTH_LONG).show();
    }

}