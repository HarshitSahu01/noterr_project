package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        buttonSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReminderMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
