package com.example.noterr_project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLInput;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBInstance.initialize(this.getApplicationContext());

        // Open Notes Section
        Button buttonOpenNotes = findViewById(R.id.buttonOpenNotes);
        buttonOpenNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesMainActivity.class);
                startActivity(intent);
            }
        });

        // Open Reminder Section
        Button buttonSetReminder = findViewById(R.id.buttonSetReminder);
        buttonSetReminder.setOnClickListener(v -> {
            Log.d("MainActivity", "Opening ReminderMainActivity...");
            Intent intent = new Intent(MainActivity.this, ReminderMainActivity.class);
            startActivity(intent);
        });
    }
}
