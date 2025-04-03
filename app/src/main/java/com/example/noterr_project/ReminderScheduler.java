package com.example.noterr_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class ReminderScheduler {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static Context appContext;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final HashMap<Integer, Runnable> scheduledTasks = new HashMap<>();
    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String ALARMS_KEY = "alarms";

    public static void init(Context context) {
        appContext = context.getApplicationContext();
        restoreAlarms();
    }

    public static void scheduleAlarm(int id, String title, String content, String dateTime) {
        if (appContext == null) {
            throw new IllegalStateException("ReminderScheduler not initialized. Call init() first.");
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(dateTime));
            long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

            if (delay > 0) {
                if (scheduledTasks.containsKey(id)) {
                    handler.removeCallbacks(scheduledTasks.get(id));
                }

                Runnable task = () -> {
                    NotificationCreator.sendNotification(id, title, content, dateTime);
                    scheduledTasks.remove(id);
                    removeAlarmFromPrefs(id);
                };

                scheduledTasks.put(id, task);
                handler.postDelayed(task, delay);
                saveAlarmToPrefs(id, title, content, dateTime);
                System.out.println("Reminder scheduled successfully: ID=" + id);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAlarm(int id) {
        if (scheduledTasks.containsKey(id)) {
            handler.removeCallbacks(scheduledTasks.get(id));
            scheduledTasks.remove(id);
        }
        removeAlarmFromPrefs(id);
    }

    private static void saveAlarmToPrefs(int id, String title, String content, String dateTime) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString(ALARMS_KEY, "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            JSONObject alarmData = new JSONObject();
            alarmData.put("title", title);
            alarmData.put("content", content);
            alarmData.put("dateTime", dateTime);
            alarms.put(String.valueOf(id), alarmData);

            prefs.edit().putString(ALARMS_KEY, alarms.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public static void restoreAlarms() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String alarmsJson = prefs.getString(ALARMS_KEY, "{}");

        try {
            JSONObject alarms = new JSONObject(alarmsJson);
            Iterator<String> keys = alarms.keys();
            while (keys.hasNext()) {
                String idStr = keys.next();
                int id = Integer.parseInt(idStr);
                JSONObject alarmData = alarms.getJSONObject(idStr);
                String title = alarmData.getString("title");
                String content = alarmData.getString("content");
                String dateTime = alarmData.getString("dateTime");
                scheduleAlarm(id, title, content, dateTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
