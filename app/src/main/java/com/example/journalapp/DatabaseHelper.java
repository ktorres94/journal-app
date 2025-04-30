package com.example.journalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ENTRY = "entry";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ENTRY + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Retrieve all journal entries
    public Cursor getAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_ENTRY}, null, null, null, null, null);
    }

    // Delete entry by id
    public boolean deleteEntry(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return deleted > 0;
    }
}