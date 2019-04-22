package com.example.boris.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boris.notes.managers.SQLBrains;
import com.example.boris.notes.models.NoteItem;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddNote extends AppCompatActivity {
    // add animation. where text from recycler moves to the editText in this activity
    private SQLBrains sqlBrains;
    private EditText editText;
    private boolean save = true;
    private long date = 0;
    private String oldText;
    private ShareActionProvider shareActionProvider;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        
        sqlBrains = new SQLBrains(getApplicationContext());
        editText = findViewById(R.id.addNoteText);

        Intent intent = getIntent();
        date = intent.getLongExtra("date", 0);
        oldText = intent.getStringExtra("text") == null ? "" : intent.getStringExtra("text");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        if (save && date == 0){
            Toast.makeText(getApplicationContext(), "adding note", Toast.LENGTH_LONG).show();
            String str = editText.getText().toString();
            if (validateString(str)){
                Observable.range(1, 1)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                v -> {
                                    date = new Date().getTime();
                                    sqlBrains.addNote(new NoteItem(date, str));
                                }
                        );
            }
        }else{
            Toast.makeText(getApplicationContext(), "editing note", Toast.LENGTH_LONG).show();
            String str = editText.getText().toString();
            if (validateString(str)){
                Observable.range(1, 1)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                v -> {
                                    //date = new Date().getTime();
                                    sqlBrains.update(new NoteItem(date, str));
                                }
                        );
            }
        }

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
}
