package com.example.journalapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewEntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        EditText journalInput = findViewById(R.id.editJournal);
        Button saveButton = findViewById(R.id.btnSave);

        saveButton.setOnClickListener(v -> {
            String text = journalInput.getText().toString();
            Toast.makeText(this, "Entry saved (not really yet): " + text, Toast.LENGTH_SHORT).show();
        });
    }
}