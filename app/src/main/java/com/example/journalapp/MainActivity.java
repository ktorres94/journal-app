package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newEntryButton = findViewById(R.id.btnNewEntry);
        Button viewEntriesButton = findViewById(R.id.btnViewEntries);

        if (newEntryButton != null) {
            newEntryButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(intent);
            });
        }

        if (viewEntriesButton != null) {
            viewEntriesButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ViewEntriesActivity.class);
                startActivity(intent);
            });
        }
    }
}