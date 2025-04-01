package com.example.noterr_project;

import android.content.Context;
import android.widget.Toast;

public class NotificationManager {
    private static Context appContext;
    public static void init(Context context) {
        appContext = context;
    }
    public static void sendNotification(int id, String title, String content, String time) {
        System.out.println("Meow meow notitication: " + id + " " + title + " " + time);
        Toast.makeText(appContext, "THIS IS WORKING BABYYYY", Toast.LENGTH_SHORT).show();
    }
}
