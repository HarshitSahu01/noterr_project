package com.example.noterr_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReminderMainActivity extends AppCompatActivity {
    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);

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
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void loadReminders() {
        reminderContainer.removeAllViews();
        Reminder[] reminders = Reminder.getReminders();
        for (Reminder reminder : reminders) {
            View reminderCard = createCard(reminder.id, reminder.title, reminder.content, reminder.time);
            reminderContainer.addView(reminderCard);
        }
    }

    private View createCard(final int reminderId, String title, String content, String time) {
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        cardView.setCardElevation(8);
        cardView.setRadius(16);
        cardView.setPadding(16, 16, 16, 16);
        cardView.setUseCompatPadding(true);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.cardBackground));

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);

        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(20);
        titleText.setTextColor(getResources().getColor(R.color.white));

        TextView contentText = new TextView(this);
        contentText.setText(content);
        contentText.setTextSize(16);
        contentText.setTextColor(getResources().getColor(R.color.gray));

        TextView timeText = new TextView(this);
        timeText.setText(time);
        timeText.setTextSize(12);
        timeText.setTextColor(getResources().getColor(R.color.timestampColor));

        cardContent.addView(titleText);
        cardContent.addView(contentText);
        cardContent.addView(timeText);
        cardView.addView(cardContent);

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(ReminderMainActivity.this, ReminderEditorActivity.class);
            intent.putExtra("reminderId", reminderId);
            startActivity(intent);
        });

        return cardView;
    }
}
