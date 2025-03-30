package com.example.noterr_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Remainder {
    int id;
    String title = "";
    String content = "";
    String time = "";
    String created_on;
    String modified_on;
    SQLiteDatabase db;

    private static Remainder[] remainders = {
            new Remainder(1, "Remainder 1", "Description 1", "10:00 AM", "", ""),
            new Remainder(2, "Remainder 2", "Description 2", "2:00 PM", "", ""),
            new Remainder(3, "Remainder 3", "Description 3", "5:30 PM", "", "")
    };

    public Remainder() {
        db = DBInstance.getInstance();
        db.execSQL("INSERT INTO remainders (title, content, time) VALUES ('', '', '')");
        try (Cursor c = db.rawQuery("SELECT id, created_on, modified_on FROM remainders WHERE id = (SELECT max(id) FROM remainders)", null)) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                created_on = c.getString(1);
                modified_on = c.getString(2);
            }
        }
    }

    private Remainder(int id, String title, String content, String time, String created_on, String modified_on) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.created_on = created_on;
        this.modified_on = modified_on;
        db = DBInstance.getInstance();
    }

    public void setTitle(String title) {
        this.title = title.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("UPDATE remainders SET title = ? WHERE id = ?", new String[]{title, Integer.toString(id)});
    }

    public void setContent(String content) {
        this.content = content.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("UPDATE remainders SET content = ? WHERE id = ?", new String[]{content, Integer.toString(id)});
    }

    public void setTime(String time) {
        this.time = time.replaceAll("(['\"\\\\])", "\\\\$1");
        db.execSQL("UPDATE remainders SET time = ? WHERE id = ?", new String[]{time, Integer.toString(id)});
    }

    public static Remainder[] getRemainders() {
        SQLiteDatabase db = DBInstance.getInstance();
        try (Cursor c = db.rawQuery("SELECT * FROM remainders", null)) {
            Remainder[] remainders = new Remainder[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                remainders[i++] = new Remainder(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5)
                );
            }
            return remainders;
        }
    }

    public static Remainder getRemainder(int id) {
        SQLiteDatabase db = DBInstance.getInstance();
        try (Cursor c = db.rawQuery("SELECT * FROM remainders WHERE id = ?", new String[]{Integer.toString(id)})) {
            if (!c.moveToNext()) return null;
            return new Remainder(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );
        }
    }

    public int delete() {
        return db.delete("remainders", "id = ?", new String[]{Integer.toString(id)});
    }

    public void save() {
        // Add custom save logic if needed
    }
}
