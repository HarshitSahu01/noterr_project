package com.example.noterr_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Reminder {
    int id;
    String title;
    String content;
    String time;
    String created_on;
    String modified_on;
    private static SQLiteDatabase db = DBInstance.getInstance();

    public Reminder() {
        db.execSQL("INSERT INTO reminders (title, content, time) VALUES ('', '', '')");
        try (Cursor c = db.rawQuery("SELECT id, created_on, modified_on FROM reminders WHERE id = (SELECT max(id) FROM reminders)", null)) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                created_on = c.getString(1);
                modified_on = c.getString(2);
            }
        }
        this.title = "";
        this.content = "";
        this.time = "";
    }

    private Reminder(int id, String title, String content, String time, String created_on, String modified_on) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.created_on = created_on;
        this.modified_on = modified_on;
    }

    public void setTitle(String title) {
        this.title = title;
        db.execSQL("UPDATE reminders SET title = ? WHERE id = ?", new String[]{title, String.valueOf(id)});
    }

    public void setContent(String content) {
        this.content = content;
        db.execSQL("UPDATE reminders SET content = ? WHERE id = ?", new String[]{content, String.valueOf(id)});
    }

    public void setTime(String time) {
        this.time = time;
        db.execSQL("UPDATE reminders SET time = ? WHERE id = ?", new String[]{time, String.valueOf(id)});
    }

    public static Reminder[] getReminders() {
        if (db == null) db = DBInstance.getInstance(); // Ensure DB is initialized
        try (Cursor c = db.rawQuery("SELECT * FROM reminders", null)) {
            Reminder[] reminders = new Reminder[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                reminders[i++] = new Reminder(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5)
                );
            }
            return reminders;
        }
    }

    public static Reminder getReminder(int id) {
        if (db == null) db = DBInstance.getInstance();
        try (Cursor c = db.rawQuery("SELECT * FROM reminders WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToNext()) return null;
            return new Reminder(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );
        }
    }
    public static int getReminderId(String title) {
        SQLiteDatabase db = DBInstance.getInstance();
        try (Cursor c = db.rawQuery("SELECT id FROM reminders WHERE title = ?", new String[]{title})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return -1; // Return -1 if not found
    }
    public int delete() {
        return db.delete("reminders", "id = ?", new String[]{String.valueOf(id)});
    }
}
