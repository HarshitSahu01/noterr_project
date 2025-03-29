package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        // Setup RecyclerView
        recyclerViewReminders = findViewById(R.id.recyclerViewReminders);
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(this));

        // Floating Action Button to Add New Reminder
        FloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);
        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ReminderEditorActivity
                Intent intent = new Intent(ReminderMainActivity.this, ReminderEditorActivity.class);
                startActivity(intent);
            }
        });
    }
}
