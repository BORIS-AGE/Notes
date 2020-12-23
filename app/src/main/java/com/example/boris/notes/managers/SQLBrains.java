package com.example.boris.notes.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.boris.notes.Constants;
import com.example.boris.notes.MainActivity;
import com.example.boris.notes.models.NoteItem;
import java.util.ArrayList;
import java.util.List;

public class SQLBrains extends SQLiteOpenHelper {

    private static final String tableName = "notes";

    private final String user_id = "user_id";

    public SQLBrains(Context context) {
        super(context, "NAME", null, Constants.BD_VERSION);
        Log.d("database operations", "database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "create table IF NOT EXISTS " + tableName + " (body text, date number, " + user_id +
                " text)");
        Log.d("database operations", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tableName);
        onCreate(db);
        Log.d("database operations", "upgrade created");
    }

    public void addNote(NoteItem item, String userId) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("body", item.getBody());
        contentValues.put("date", item.getDate());
        contentValues.put(user_id, userId);

        database.insert(tableName, null, contentValues);

        database.close();
    }

    public List<NoteItem> getNotes(String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] required = { "body", "date" };
        Cursor cursor = database.query(
            tableName,
            required,
            user_id + " = " + userId,
            null,
            null,
            null,
            "date " + MainActivity.order,
            "0, 20"
        );

        List<NoteItem> noteItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            noteItems.add(new NoteItem(date, body));
        }

        database.close();
        return noteItems;
    }

    public List<NoteItem> getNotes(String query, String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] required = { "body", "date" };
        Cursor cursor = database.query(
            tableName,
            required,
            "body like '%" + query + "%'" + " AND " + user_id + " = " + userId,
            null,
            null,
            null,
            "date " + MainActivity.order
        );

        List<NoteItem> noteItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            noteItems.add(new NoteItem(date, body));
        }

        database.close();
        return noteItems;
    }

    public List<NoteItem> getNotes(int offset, String userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] required = { "body", "date" };
        Cursor cursor = database.query(
            tableName,
            required,
            user_id + " = " + userId,
            null,
            null,
            null,
            "date " + MainActivity.order,
            offset + ",20"
        );

        List<NoteItem> noteItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            noteItems.add(new NoteItem(date, body));
        }

        database.close();
        return noteItems;
    }

    public void update(NoteItem item, String userId) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("body", item.getBody());
        contentValues.put("date", item.getDate());

        database.update(
            tableName,
            contentValues,
            "date = " + item.getDate() + " AND " + user_id + " = " + userId,
            null
        );
        database.close();
    }

    public void delete(long date){
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(tableName, "date = " + date, null);
        database.close();
    }
}
