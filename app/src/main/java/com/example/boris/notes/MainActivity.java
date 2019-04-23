package com.example.boris.notes;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.boris.notes.adapters.RecyclerAdapter;
import com.example.boris.notes.managers.SQLBrains;
import com.example.boris.notes.models.NoteItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager manager;
    private List<NoteItem> noteItems;
    private SQLBrains sqlBrains;
    private CompositeDisposable disposable;
    private SearchView searchView;
    private boolean isScrolling = false;
    private int currentItems, scrollOutItems;
    public static String order = "DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaults();
        setRecycler();

        fab.setOnClickListener((view) -> {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            startActivity(new Intent(MainActivity.this, AddNote.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        setSearchConfig(searchView);

    }

    private void setRecycler() {
        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setDefaults() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        disposable = new CompositeDisposable();
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
        noteItems = new ArrayList<>();
        sqlBrains = new SQLBrains(getApplicationContext());
        recyclerAdapter = new RecyclerAdapter(noteItems, this);

        //search view
        searchView = findViewById(R.id.searchMain);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        LinearLayout linearLayout = findViewById(R.id.searchBox);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        //adding new elements on scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                int totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    getNotes(totalItems);
                }
            }
        });

    }

    private void getNotes() {
        disposable.add(
                Observable.just(sqlBrains.getNotes())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                v -> {
                                    noteItems.addAll(v);
                                }
                        ));
    }

    private void getNotes(int offset) {
        int temp = noteItems.size();
        disposable.add(
                Observable.just(sqlBrains.getNotes(offset))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> {
                            //recyclerAdapter.removeLoadingFooter();
                            recyclerAdapter.updateList(noteItems);
                        })
                        .subscribe(
                                v -> {
                                    //recyclerAdapter.addLoadingFooter();
                                    noteItems.addAll(v);
                                }
                        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selection_of_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_menu_from_new:
                noteItems.clear();
                order = "DESC";
                getNotes(0);
                return true;
            case R.id.action_menu_from_old:
                noteItems.clear();
                order = "ASC";
                getNotes(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    public void setSearchConfig(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (!query.trim().equals(""))
                    disposable.add(
                            Observable.just(sqlBrains.getNotes(query))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            v -> {
                                                recyclerAdapter.updateList(v);
                                            }
                                    ));
                else
                    recyclerAdapter.updateList(noteItems);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteItems.clear();
        getNotes();
        setRecycler();
    }

}
