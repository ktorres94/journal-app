package com.example.journalapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewEntryActivity extends AppCompatActivity {
    EditText editText;
    Button saveButton;
    DatabaseHelper dbHelper;
    int entryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        editText = findViewById(R.id.editJournal);
        saveButton = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("entryId")) {
            entryId = intent.getIntExtra("entryId", -1);
            String entryText = intent.getStringExtra("entryText");
            editText.setText(entryText);
        }

        saveButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_ENTRY, text);

                if (entryId == -1) {
                    // New entry
                    long result = db.insert(DatabaseHelper.TABLE_NAME, null, values);
                    showResult(result != -1);
                } else {
                    // Update existing entry
                    int result = db.update(DatabaseHelper.TABLE_NAME, values, "_id=?", new String[]{String.valueOf(entryId)});
                    showResult(result > 0);
                }
            } else {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResult(boolean success) {
        Toast.makeText(this, success ? "Entry saved!" : "Error saving entry", Toast.LENGTH_SHORT).show();
        if (success) finish(); // go back to previous screen
    }
}