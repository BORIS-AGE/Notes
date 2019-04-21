package com.example.boris.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.boris.notes.adapters.RecyclerAdapter;
import com.example.boris.notes.managers.SQLBrains;
import com.example.boris.notes.models.NoteItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<NoteItem> noteItems;
    private SQLBrains sqlBrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaults();
        setRecycler();

        fab.setOnClickListener((view)->{
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(MainActivity.this, AddNote.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
    }

    private void setRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setDefaults() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
        noteItems = new ArrayList<>();
        sqlBrains = new SQLBrains(getApplicationContext());
        getNotes(0);
        recyclerAdapter = new RecyclerAdapter(noteItems, getApplicationContext());
    }

    private void getNotes(int index) {
        noteItems.addAll(sqlBrains.getNotes());

        for (int i = 0; i < 20; i++) {
            noteItems.add(new NoteItem(new Date().getTime(), "dwaaaaaaaaaaaaaaaaaaaaaaaaawdawdawdawdawdawdawdawdawd"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
