package com.example.journalapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewEntriesActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;
    private ArrayList<String> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewEntries);
        entries = new ArrayList<>();

        loadEntries();
    }

    private void loadEntries() {
        entries.clear();
        Cursor cursor = dbHelper.getAllEntries();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String entryText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY));
                String timestamp = "";
                int timestampIndex = cursor.getColumnIndex("timestamp");
                if (timestampIndex != -1) {
                    timestamp = cursor.getString(timestampIndex);
                }
                String entry = (timestamp.isEmpty() ? "" : timestamp + ":\n") + entryText;
                entries.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);

        // Set up click to edit entry
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor editCursor = dbHelper.getAllEntries();
            if (editCursor.moveToPosition(position)) {
                long entryId = editCursor.getLong(editCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String entryText = editCursor.getString(editCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY));
                editCursor.close();

                android.content.Intent intent = new android.content.Intent(ViewEntriesActivity.this, com.example.journalapp.NewEntryActivity.class);
                intent.putExtra("entryId", entryId);
                intent.putExtra("entryText", entryText);
                startActivity(intent);
            }
        });

        // Set up long click to delete entry with confirmation dialog
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new android.app.AlertDialog.Builder(ViewEntriesActivity.this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Cursor deleteCursor = dbHelper.getAllEntries();
                    if (deleteCursor.moveToPosition(position)) {
                        long entryId = deleteCursor.getLong(deleteCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                        boolean deleted = dbHelper.deleteEntry(entryId);
                        if (deleted) {
                            entries.remove(position);
                            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
                            android.widget.Toast.makeText(ViewEntriesActivity.this, "Entry deleted", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            android.widget.Toast.makeText(ViewEntriesActivity.this, "Failed to delete", android.widget.Toast.LENGTH_SHORT).show();
                        }
                        deleteCursor.close();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEntries();
    }
}