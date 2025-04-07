package com.example.noterr_project;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    protected void test() {
//        NotificationManager.sendNotification(1, "HI", "BYE", "121:11");
//        ReminderScheduler.scheduleAlarm(1, "test1", "nothing", "03/04/2025 16:30");
//        Log.e("Data", "Function  called");
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        DBInstance.initialize(this.getApplicationContext());

        NotificationCreator.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
        ReminderScheduler.init(this);

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        // Open Notes Section
        CardView buttonOpenNotes = findViewById(R.id.buttonOpenNotes);
        buttonOpenNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesMainActivity.class);
                startActivity(intent);
            }
        });

        // Open Reminder Section
        CardView buttonSetReminder = findViewById(R.id.buttonSetReminder);
        buttonSetReminder.setOnClickListener(v -> {
            Log.d("MainActivity", "Opening ReminderMainActivity...");
            Intent intent = new Intent(MainActivity.this, ReminderMainActivity.class);
            startActivity(intent);
        });
    }
}
