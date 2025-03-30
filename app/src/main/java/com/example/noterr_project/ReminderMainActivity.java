package com.example.noterr_project;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.cardview.widget.CardView;

public class ReminderMainActivity extends AppCompatActivity {

    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

        // Initialize Database
        DBInstance.initialize(getApplicationContext());

        // Initialize the container where reminders will be added dynamically
        reminderContainer = findViewById(R.id.remindersContainer);

        if (reminderContainer == null) {
            Log.e("ReminderMainActivity", "remindersContainer is NULL. Check XML ID.");
            return;
        }

        // Load existing reminders
        Reminder[] reminders = Reminder.getReminders();

        if (reminders == null || reminders.length == 0) {
            Log.e("ReminderMainActivity", "No reminders found.");
        } else {
            for (Reminder reminder : reminders) {
                addReminderCard(reminder);
            }
        }

        // Floating Action Button to add a new reminder
        FloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);
        fabAddReminder.setOnClickListener(view -> {
            Reminder newReminder = new Reminder();
            addReminderCard(newReminder);
        });
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
        titleTextView.setTypeface(null, Typeface.BOLD);

        TextView contentTextView = new TextView(this);
        contentTextView.setText(reminder.content);
        contentTextView.setTextSize(14);

        cardLayout.addView(titleTextView);
        cardLayout.addView(contentTextView);
        cardView.addView(cardLayout);
        reminderContainer.addView(cardView);
    }
}
