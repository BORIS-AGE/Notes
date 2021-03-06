package com.example.boris.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boris.notes.managers.SQLBrains;
import com.example.boris.notes.models.NoteItem;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class AddNote extends AppCompatActivity {
    // add animation. where text from recycler moves to the editText in this activity
    private SQLBrains sqlBrains;
    private EditText editText;
    private boolean save = true;
    private String userId;
    private long date = 0;
    private String oldText;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme_ActionBarStyle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        loadUserId();
        sqlBrains = new SQLBrains(getApplicationContext());
        editText = findViewById(R.id.addNoteText);

        //getting old values for editing if they are
        Intent intent = getIntent();
        date = intent.getLongExtra("date", 0);
        oldText = intent.getStringExtra("text") == null ? "" : intent.getStringExtra("text");
        editText.setText(oldText);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText.requestFocus();
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    void loadUserId() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFERENCES_KEY, MODE_PRIVATE);
        userId = sPref.getString(Constants.USER_ID, "");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        if (save && date == 0){
            String str = editText.getText().toString();
            if (validateString(str)){
                Toast.makeText(getApplicationContext(), "adding note", Toast.LENGTH_LONG).show();
                disposable.add(Observable.range(1, 1)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                v -> {
                                    date = new Date().getTime();
                                    sqlBrains.addNote(new NoteItem(date, str), userId);
                                }
                        ));
            }
        }else{
            String str = editText.getText().toString();
            if (validateString(str) && save && !str.equals(oldText)){
                Toast.makeText(getApplicationContext(), "editing note", Toast.LENGTH_LONG).show();
                disposable.add(Observable.range(1, 1)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                v -> {
                                    //date = new Date().getTime();
                                    sqlBrains.update(new NoteItem(date, str), userId);
                                }
                        ));
            }
        }
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_activity_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.shareIcon:
                share();
                return true;
            case R.id.deleteIcon:
                delete();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        save = false;
        if (date != 0){
            SQLBrains brains = new SQLBrains(getApplicationContext());
            brains.delete(date);
            onBackPressed();
        }
    }

    private void share() {
        String str = editText.getText().toString();
        if (validateString(str)){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, str);
            startActivity(Intent.createChooser(intent, "Share Via"));
        }


    }

    private boolean validateString(String str) {
        if (str.trim().equals("")){
            Toast.makeText(getApplicationContext(), "invalid sting", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

}
