package com.example.boris.notes.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.boris.notes.models.NoteItem;

import java.util.ArrayList;
import java.util.List;

public class SQLBrains extends SQLiteOpenHelper {

    private static final String tableName = "notes";

    public SQLBrains(Context context){
        super(context, "NAME", null, 1);
        Log.d("database operations", "database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + tableName + " (body text, date number)");
        Log.d("database operations", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tableName);
        onCreate(db);
        Log.d("database operations", "upgrade created");
    }

    public void addNote(NoteItem item){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("body", item.getBody());
        contentValues.put("date", item.getDate());

        database.insert(tableName, null, contentValues);

        database.close();
    }

    public List<NoteItem> getNotes(){
        SQLiteDatabase database = this.getReadableDatabase();
        String[] required = {"body", "date"};
        Cursor cursor = database.query(tableName,required,null,null,null,null,"date");

        List<NoteItem> noteItems = new ArrayList<>();
        while (cursor.moveToNext()){
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            noteItems.add(new NoteItem(date, body));
        }

        database.close();
        return noteItems;
    }

    public void update(NoteItem item){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("body", item.getBody());
        contentValues.put("date", item.getDate());

        database.update(tableName,contentValues, "date = " + item.getDate(),null);
        database.close();
    }

    public void delete(long date){
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(tableName, "date = " + date, null);
        database.close();
    }
}
