package com.example.noterr_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReminders();
    }

    private void loadReminders() {
        reminderContainer.removeAllViews();
        Reminder[] reminders = Reminder.getReminders();
        for (Reminder reminder : reminders) {
            View reminderCard = createCard(reminder.id, reminder.title, reminder.content, reminder.time);
            reminderContainer.addView(reminderCard);
        }
    }
    private View createCard(final int reminderId, String title, String content, String timestamp) {
        CardView cardView = new CardView(this);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        cardParams.setMargins(0, dpToPx(12), 0, 0);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(dpToPx(6));
        cardView.setRadius(dpToPx(12));
        cardView.setCardBackgroundColor(Color.parseColor("#3E1E68"));

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        relativeLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        TextView titleText = new TextView(this);
        titleText.setId(View.generateViewId());
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(Color.WHITE);

        TextView contentText = new TextView(this);
        contentText.setText(content);
        contentText.setTextSize(14);
        contentText.setTextColor(Color.WHITE);

        TextView timestampText = new TextView(this);
        timestampText.setText(timestamp);
        timestampText.setTextSize(12);
        timestampText.setTextColor(Color.parseColor("#B39DDB"));

        ImageView deleteButton = new ImageView(this);
        deleteButton.setImageResource(R.drawable.ic_delete);
        deleteButton.setColorFilter(Color.WHITE);
        deleteButton.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        titleText.setLayoutParams(titleParams);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        contentParams.addRule(RelativeLayout.BELOW, titleText.getId());
        contentText.setLayoutParams(contentParams);

        RelativeLayout.LayoutParams timestampParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        timestampText.setLayoutParams(timestampParams);

        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(
                dpToPx(36),
                dpToPx(36)
        );
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        deleteButton.setLayoutParams(deleteParams);

        relativeLayout.addView(titleText);
        relativeLayout.addView(contentText);
        relativeLayout.addView(timestampText);
        relativeLayout.addView(deleteButton);
        cardView.addView(relativeLayout);

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(ReminderMainActivity.this, ReminderEditorActivity.class);
            intent.putExtra("reminderId", reminderId);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> showDeleteConfirmation(reminderId));

        return cardView;
    }
    private void showDeleteConfirmation(final int reminderId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Reminder.deleteReminder(reminderId);
                    loadReminders();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
