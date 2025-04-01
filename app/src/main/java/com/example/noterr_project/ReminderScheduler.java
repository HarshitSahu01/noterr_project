package com.example.noterr_project;

import android.app.AlarmManager;
import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import android.os.Handler;
import android.os.Looper;

public class ReminderScheduler {
    private static final Map<Integer, Runnable> scheduledTasks = new ConcurrentHashMap<>();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void scheduleAlarm(Context context, int id, String title, String content, String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(format.parse(dateTime));
        } catch (ParseException e) {
            return;
        }

        long triggerTime = calendar.getTimeInMillis();
        if (scheduledTasks.containsKey(id)) {
            handler.removeCallbacks(scheduledTasks.get(id));
        }

        Runnable task = () -> NotificationManager.sendNotification(id, title, content);
        scheduledTasks.put(id, task);
        handler.postDelayed(task, triggerTime - System.currentTimeMillis());
    }
}
