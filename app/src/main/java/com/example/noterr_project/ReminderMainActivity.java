package com.example.noterr_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

    private View createCard(final int noteId, String title, String content, String timestamp) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(100)
        );
        cardParams.setMargins(0, dpToPx(12), 0, 0);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(dpToPx(6));
        cardView.setRadius(dpToPx(12));
        cardView.setCardBackgroundColor(Color.parseColor("#3E1E68"));

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        relativeLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        TextView titleText = new TextView(this);
        titleText.setId(View.generateViewId());
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        titleText.setLayoutParams(titleParams);

        TextView contentText = new TextView(this);
        contentText.setId(View.generateViewId());
        contentText.setText(content);
        contentText.setTextSize(14);
        contentText.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        contentParams.addRule(RelativeLayout.BELOW, titleText.getId());
        contentText.setLayoutParams(contentParams);

        TextView timestampText = new TextView(this);
        timestampText.setText(timestamp);
        timestampText.setTextSize(12);
        timestampText.setTextColor(Color.parseColor("#B39DDB"));
        RelativeLayout.LayoutParams timestampParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        timestampParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        timestampText.setLayoutParams(timestampParams);

        relativeLayout.addView(titleText);
        relativeLayout.addView(contentText);
        relativeLayout.addView(timestampText);
        cardView.addView(relativeLayout);

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(ReminderMainActivity.this, NotesEditorActivity.class);
            intent.putExtra("noteId", noteId);
            startActivity(intent);
        });

        return cardView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
