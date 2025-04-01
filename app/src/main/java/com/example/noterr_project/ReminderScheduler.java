package com.example.noterr_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderScheduler {
    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String ALARMS_KEY = "alarms";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    private static Context appContext;

    // Initialize with application context
    public static void init(Context context) {
        appContext = context.getApplicationContext();
        restoreAlarms();
    }

    // Schedule a new alarm or update existing one
    public static void scheduleAlarm(int id, String title, String content, String dateTime) {
        if (appContext == null) {
            throw new IllegalStateException("ReminderManager not initialized. Call init() first.");
        }

        try {
            // Parse date and calculate trigger time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(dateTime));
            long triggerTime = calendar.getTimeInMillis();

            // Create intent for the alarm
            Intent intent = new Intent(appContext, ReminderReceiver.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("time", dateTime);

            // Setup pending intent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    appContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Get alarm manager and set alarm
            AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }

            // Save alarm data to preferences
            saveAlarmToPrefs(id, title, content, dateTime, triggerTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Delete an existing alarm
    public static void deleteAlarm(int id) {
        if (appContext == null) {
            throw new IllegalStateException("ReminderManager not initialized. Call init() first.");
        }

        // Cancel the alarm
        Intent intent = new Intent(appContext, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                appContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        // Remove from preferences
        removeAlarmFromPrefs(id);
    }

    // Save alarm data to SharedPreferences
    private static void saveAlarmToPrefs(int id, String title, String content,
                                         String dateTime, long triggerTime) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString(ALARMS_KEY, "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            JSONObject alarmData = new JSONObject();
            alarmData.put("title", title);
            alarmData.put("content", content);
            alarmData.put("dateTime", dateTime);
            alarmData.put("triggerTime", triggerTime);
            alarms.put(String.valueOf(id), alarmData);

            prefs.edit().putString(ALARMS_KEY, alarms.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Remove alarm data from SharedPreferences
    private static void removeAlarmFromPrefs(int id) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString(ALARMS_KEY, "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            if (alarms.has(String.valueOf(id))) {
                alarms.remove(String.valueOf(id));
                prefs.edit().putString(ALARMS_KEY, alarms.toString()).apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Restore all alarms from SharedPreferences
    public static void restoreAlarms() {
        if (appContext == null) {
            throw new IllegalStateException("ReminderManager not initialized. Call init() first.");
        }

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString(ALARMS_KEY, "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            java.util.Iterator<String> keys = alarms.keys();
            while (keys.hasNext()) {
                String idStr = keys.next();

                int id = Integer.parseInt(idStr);
                JSONObject alarmData = alarms.getJSONObject(idStr);
                String title = alarmData.getString("title");
                String content = alarmData.getString("content");
                String dateTime = alarmData.getString("dateTime");
                long triggerTime = alarmData.getLong("triggerTime");

                // Only reschedule if alarm hasn't triggered yet
                if (triggerTime > System.currentTimeMillis()) {
                    scheduleAlarm(id, title, content, dateTime);
                } else {
                    // Clean up past alarms
                    removeAlarmFromPrefs(id);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

// Receiver class to handle alarm triggers
class ReminderReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String dateTime = intent.getStringExtra("time");

        // Trigger notification
        NotificationManager.sendNotification(id, title, content, dateTime);

        // Remove triggered alarm from preferences
        SharedPreferences prefs = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString("alarms", "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            if (alarms.has(String.valueOf(id))) {
                alarms.remove(String.valueOf(id));
                prefs.edit().putString("alarms", alarms.toString()).apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}