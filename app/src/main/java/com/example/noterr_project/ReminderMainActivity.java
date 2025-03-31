package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReminderMainActivity extends AppCompatActivity {

    private static final int CREATE_REMINDER_REQUEST = 1;
    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        // Initialize Toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Task Reminders");
        }

        reminderContainer = findViewById(R.id.remindersContainer);
        FloatingActionButton fab = findViewById(R.id.fabAddReminder);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReminderEditorActivity.class);
            startActivityForResult(intent, CREATE_REMINDER_REQUEST);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        loadReminders();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_REMINDER_REQUEST) {
            loadReminders();
        }
    }

    private void loadReminders() {
        reminderContainer.removeAllViews();
        Reminder[] reminders = Reminder.getReminders();

        if (reminders != null) {
            for (Reminder reminder : reminders) {
                addReminderCard(reminder);
            }
        }
    }

    private void addReminderCard(Reminder reminder) {
        // Your existing card creation code
    }
}