package com.example.boris.notes.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.boris.notes.Constants;

public class AuthSQLBrains extends SQLiteOpenHelper {

    private static final String tableName = "users";

    private final String email_field = "email";

    private final String password_field = "password";

    private final String id_field = "id";

    public AuthSQLBrains(Context context) {
        super(context, "NAME", null, Constants.BD_VERSION);
        Log.d("database operations", "database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + tableName +
            " (" + email_field + " text, " + password_field + " text, " + id_field + " text)");
        db.execSQL("create table IF NOT EXISTS notes (body text, date number, user_id text)");
        Log.d("database operations", "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tableName);
        db.execSQL("drop table if exists notes");
        onCreate(db);
        Log.d("database operations", "upgrade created");
    }

    public String saveUser(final String email, final String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        int id = (int) System.currentTimeMillis();
        if (id < 0) {
            id *= -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(email_field, email);
        contentValues.put(password_field, password);
        contentValues.put(id_field, id);

        database.insert(tableName, null, contentValues);

        database.close();
        return id + "";
    }

    public String findUser(final String email, final String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] required = { id_field };
        Cursor cursor = database.query(
            tableName,
            required,
            email_field + " = " + email + " AND " + password_field + " = " + password,
            null,
            null,
            null,
            null
        );

        if (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(id_field));

            database.close();
            return id;
        } else {
            database.close();
            return "";
        }
    }

}