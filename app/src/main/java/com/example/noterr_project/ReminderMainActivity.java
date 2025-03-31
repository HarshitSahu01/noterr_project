package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReminderMainActivity extends AppCompatActivity {

    private static final int CREATE_REMINDER_REQUEST = 1;
    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Task Reminders");
        }

        reminderContainer = findViewById(R.id.remindersContainer);
        FloatingActionButton fab = findViewById(R.id.fabAddReminder);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReminderEditorActivity.class);
            startActivityForResult(intent, CREATE_REMINDER_REQUEST);
        });

        loadReminders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReminders(); // Refresh when returning to activity
    }

    private void loadReminders() {
        reminderContainer.removeAllViews();
        Reminder[] reminders = Reminder.getReminders();

        if (reminders != null && reminders.length > 0) {
            for (Reminder reminder : reminders) {
                addReminderCard(reminder);
            }
        }
    }

    private void addReminderCard(Reminder reminder) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 16, 16, 16);
        cardView.setLayoutParams(layoutParams);
        cardView.setCardElevation(8);
        cardView.setRadius(16);
        cardView.setPadding(24, 24, 24, 24);

        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleTextView = new TextView(this);
        titleTextView.setText(reminder.title);
        titleTextView.setTextSize(18);
        titleTextView.setTextColor(getResources().getColor(android.R.color.black));

        TextView contentTextView = new TextView(this);
        contentTextView.setText(reminder.content);
        contentTextView.setTextSize(14);
        contentTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

        TextView timeTextView = new TextView(this);
        timeTextView.setText(reminder.time);
        timeTextView.setTextSize(12);
        timeTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

        cardLayout.addView(titleTextView);
        cardLayout.addView(contentTextView);
        cardLayout.addView(timeTextView);
        cardView.addView(cardLayout);

        // Make card clickable for editing
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReminderEditorActivity.class);
            intent.putExtra("REMINDER_ID", reminder.id);
            startActivityForResult(intent, CREATE_REMINDER_REQUEST);
        });

        reminderContainer.addView(cardView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_REMINDER_REQUEST && resultCode == RESULT_OK) {
            loadReminders(); // Refresh after editing/adding
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}