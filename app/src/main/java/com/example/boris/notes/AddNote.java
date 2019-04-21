package com.example.boris.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddNote extends AppCompatActivity {
    // add animation. where text from recycler moves to the editText in this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
