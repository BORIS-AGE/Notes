package com.example.boris.notes.auth;

import android.content.Context;
import android.service.autofill.RegexValidator;
import com.example.boris.notes.R;
import com.example.boris.notes.managers.AuthSQLBrains;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AuthPresenter {

    private final AuthSQLBrains sqlBrains;

    private final AuthView viewState;

    CompositeDisposable disposable = new CompositeDisposable();

    private String email = "";

    private String password = "";

    AuthPresenter(AuthView viewState, Context context) {
        this.viewState = viewState;
        sqlBrains = new AuthSQLBrains(context);
    }

    void fieldEntered(String value, FieldType type) {
        switch (type) {
            case EMAIL:
                email = value;
                break;
            case PASSWORD:
                password = value;
                break;
        }
    }

    void saveValue() {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() && !password.isEmpty()) {
            disposable.add(
                Observable.just(sqlBrains.findUser(email, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(viewState::openMainActivity)
                    .subscribe(
                        v -> {
                            String id = sqlBrains.findUser(email, password);
                            if (id.equals("")) {
                                disposable.add(Observable.just(sqlBrains.saveUser(email, password))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(viewState::saveIdToPreferences));
                            } else {
                                viewState.saveIdToPreferences(id);
                            }
                        }
                    )
            );
        } else {
            viewState.showError(R.string.fields_error_text);
        }
    }

    public enum FieldType {
        EMAIL,
        PASSWORD;
    }
}